package yuan.flood.service;

import com.google.common.base.Strings;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import yuan.flood.dao.Entity.DetectedEvent;
import yuan.flood.dao.Entity.DetectedFullEvent;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.IDao.IDetectedEventDao;
import yuan.flood.dao.IDao.IDetectedFullEventDao;
import yuan.flood.dao.IDao.IEventDao;
import yuan.flood.phase.IPhaseService;
import yuan.flood.phase.PhaseFactory;
import yuan.flood.service.IService.*;
import yuan.flood.until.SOSSESConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Yuan on 2017/2/17.
 */
@Service
public class DecodeWNSEventService implements IDecodeWNSEventService {

    //事件状态消息
    private final static String DIAGNOSIS_TYPE = "diagnosis";
    private final static String PREPARE_TYPE = "prepare";
    private final static String RESPONSE_TYPE = "response";
    private final static String RECOVERY_TYPE = "recovery";
    //用于存储消息内容
    public static Queue<String> queue = new ConcurrentLinkedQueue<String>();
    public static Map<String, String> currentEventType = new HashMap<>();
    public static Map<String, DetectedFullEvent> lastestFullEvent = new HashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(4);
    private ScheduledExecutorService prepareExecutorService = Executors.newScheduledThreadPool(4);
    private ScheduledExecutorService responseExecutorService = Executors.newScheduledThreadPool(4);

    //用于构建定时任务线程，预测与发送
    private Map<String, ScheduledExecutorService> predictThreadPoolMap = new HashMap<>();

    //用于构建单任务线程，主要是预警、通知与统计
    private Map<String, ExecutorService> executorServiceMap = new HashMap<>();

    @Autowired
    private IDiagnosisPhaserService diagnosisPhaserService;
    @Autowired
    @Qualifier("preparePhaseServiceT")
    private IPreparePhaseServiceT preparePhaseServiceT;
    @Autowired
    private IResponsePhaseService responsePhaseService;
    @Autowired
    private IRecoveryPhaseService recoveryPhaseService;

    @Autowired
    private IEventDao eventDao;
    @Autowired
    private IDetectedEventDao detectedEventDao;
    @Autowired
    private IDetectedFullEventDao detectedFullEventDao;

    public void saveEventFromWNS() {
        try {
            ServerSocket serverSocket = new ServerSocket(8092);
            while (true) {

                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String receiveStrLine;
                StringBuffer resultXML = new StringBuffer();
                boolean isStartAppend = false;
                while ((receiveStrLine = bufferedReader.readLine()) != null) {
                    if (receiveStrLine.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"))
                        isStartAppend = true;
                    if (isStartAppend)
                        resultXML.append(receiveStrLine);
                }
                socket.close();
                bufferedReader.close();
                DetectedEvent detectedEvent = new DetectedEvent();
                String result = resultXML.toString();
                EnvelopeDocument envelopeDocument = EnvelopeDocument.Factory.parse(result);
                Envelope envelope = envelopeDocument.getEnvelope();
                Body body = envelope.getBody();
                XmlCursor cursor = body.newCursor();
                cursor.toFirstContentToken();
                cursor.toChild(0);
                cursor.toChild(2);
                cursor.toChild(0);
                //the location of DerivedEvent qname
                cursor.addToSelection();
                cursor.toChild(0);
                cursor.toChild(0);
                cursor.addToSelection();
                cursor.toChild(0);
                String startTime = cursor.getTextValue().replace("+08:00", "+0800");
                cursor.toSelection(1);
                cursor.toChild(1);
                String endTime = cursor.getTextValue().replace("+08:00", "+0800");
                // get the long type of time
                cursor.toSelection(0);
                cursor.toChild(1);
                cursor.toChild(0);
                cursor.toChild(1);
                String[] timeLongStr = cursor.getTextValue().split("until");
                Long startLongTime = Long.valueOf(timeLongStr[0]);
                Long endLongTime = Long.valueOf(timeLongStr[1]);
                cursor.toSelection(0);
                cursor.toChild(2);
                cursor.toChild(0);
                cursor.toChild(1);
                String eventMessage = cursor.getTextValue();
                queue.offer(eventMessage);

                String[] message_IdStr = eventMessage.split(":");

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                String t = simpleDateFormat.format(new Date());
                Date startDate = simpleDateFormat.parse(startTime);
                Date endDate = simpleDateFormat.parse(endTime);
                detectedEvent.setEventTypeName(message_IdStr[1]);
                detectedEvent.setStartTime(startDate);
                detectedEvent.setEndTime(endDate);
                detectedEvent.setStartTimeLong(startLongTime);
                detectedEvent.setEndTimeLong(endLongTime);

                String eventStr = "from SubscibeEventParams s where s.eventID='" + message_IdStr[0] + "'";
                List subscibeEventParams = eventDao.find(eventStr);
                detectedEvent.setEvent((SubscibeEventParams) subscibeEventParams.get(0));
                //获取消息内容，线程执行获取结果
                detectedEventDao.saveOrUpdate(detectedEvent);
                //形成事件整体
                recordCurrentEvent(message_IdStr[0], message_IdStr[1], endDate);

                //需要消息内容，事件信息，以及事件对应的服务内容
                if (isEventStatusChanged(message_IdStr[0], message_IdStr[1])) {
                    //当事件状态改变，更新事件状态
                    String lastEventType = currentEventType.get(message_IdStr[0]);
                    currentEventType.put(message_IdStr[0], message_IdStr[1]);

                    //当不是在准备和响应阶段时要关闭这两个阶段的线程池。防止出现过度计算
                    if (message_IdStr[1].equals(DIAGNOSIS_TYPE)) {
                        if (!prepareExecutorService.isShutdown()) prepareExecutorService.shutdownNow();
                        if (!responseExecutorService.isShutdown()) responseExecutorService.shutdownNow();
                    }
                    //计算服务内容
                    if (message_IdStr[1].equals(PREPARE_TYPE)) {
                        //如果准备服务的线程没有停止，则进行运算
                        if (!responseExecutorService.isShutdown()) responseExecutorService.shutdownNow();
                        if (prepareExecutorService.isShutdown())
                            prepareExecutorService = Executors.newScheduledThreadPool(1);

                        //将服务放入线程
                        prepareExecutorService.scheduleAtFixedRate(new Runnable() {
                            @Override
                            public void run() {
                                preparePhaseServiceT.executeService(message_IdStr[0], endDate);
                            }
                        }, 0, SOSSESConfig.getExexcutorservicetimeperiod(), TimeUnit.MILLISECONDS);
                    }

                    if (message_IdStr[1].equals(RESPONSE_TYPE)) {
                        //如果上一个线程没有关闭，立即关闭线程
                        if (!prepareExecutorService.isShutdown()) prepareExecutorService.shutdownNow();
                        if (responseExecutorService.isShutdown())
                            responseExecutorService = Executors.newScheduledThreadPool(1);
                        responseExecutorService.scheduleAtFixedRate(new Runnable() {
                            @Override
                            public void run() {
                                responsePhaseService.executeService(message_IdStr[0], endDate);
                            }
                        }, 0,  SOSSESConfig.getExexcutorservicetimeperiod(), TimeUnit.MILLISECONDS);
                    }
                    if (message_IdStr[1].equals(RECOVERY_TYPE)){
                        if (!prepareExecutorService.isShutdown()) prepareExecutorService.shutdownNow();
                        if (!responseExecutorService.isShutdown()) responseExecutorService.shutdownNow();
                    }
                    //状态改变，且当前状态为recovery时，进行事件构建,并存储事件
                     if (RECOVERY_TYPE.equals(lastEventType) && DIAGNOSIS_TYPE.equals(message_IdStr[1])) {
                        DetectedFullEvent detectedFullEvent = lastestFullEvent.get(message_IdStr[0]);
//                        detectedFullEventDao.save(detectedFullEvent);
                        //统计恢复阶段的数据信息`
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                recoveryPhaseService.executeService(message_IdStr[0], endDate, detectedFullEvent);
                            }
                        });
                    }

                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void getEventService() {
        //根据队列内容，返回连接和当前状态
        while (!queue.isEmpty()) {

        }
    }

    /**
     * 用于判断事件的状态是否改变，如果改变，执行对应的操作
     *
     * @param sesID
     * @param eventType
     * @return
     * @throws Exception
     */
    private boolean isEventStatusChanged(String sesID, String eventType) {
        if (Strings.isNullOrEmpty(sesID) || Strings.isNullOrEmpty(eventType)) {
            try {
                throw new Exception("sesID为空或事件装态为空");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Set<String> eventIDs = currentEventType.keySet();
        //当前不存在该事件时，将该事件存储状态，并返回状态已改变
        if (eventIDs == null || eventIDs.isEmpty()) {
            return true;
        }
        if (!eventIDs.contains(sesID)) {
            return true;
        }
        //当包含该事件时，查看状态是否改变，并更新状态
        String lastEventType = currentEventType.get(sesID);
        //状态未改变时
        if (lastEventType.equals(eventType)) {
            return false;
        }
        return true;
    }

    /**
     * 用于构建当前最近的事件
     */
    private void recordCurrentEvent(String sesID, String eventType, Date time) {
        String lastEventType = currentEventType.get(sesID);
        DetectedFullEvent detectedFullEvent;
        if (Strings.isNullOrEmpty(lastEventType)) {
            detectedFullEvent = new DetectedFullEvent();
            setDetectedFullEvent(detectedFullEvent, eventType, time, lastEventType);
            detectedFullEvent.setStartTime(time);
            lastestFullEvent.put(sesID, detectedFullEvent);
            return;
        }
        //主要有准备阶段的状态，就进行更新
        if (DIAGNOSIS_TYPE.equals(eventType)&&!lastEventType.equals(RECOVERY_TYPE)){
            detectedFullEvent = lastestFullEvent.get(sesID);
            if (detectedFullEvent==null) detectedFullEvent = new DetectedFullEvent();
            detectedFullEvent.setDiagnosisStartTime(time);
            detectedFullEvent.setStartTime(time);
            lastestFullEvent.put(sesID, detectedFullEvent);
            return;
        }

        if (!isEventStatusChanged(sesID, eventType)) return;

        //如果事件状态改变，则记录事件的发生时间
        Set<String> eventIDs = lastestFullEvent.keySet();
        if (eventIDs == null || eventIDs.isEmpty()) {
            detectedFullEvent = new DetectedFullEvent();
            setDetectedFullEvent(detectedFullEvent, eventType, time, lastEventType);
            lastestFullEvent.put(sesID, detectedFullEvent);
            return;
        }
        detectedFullEvent = lastestFullEvent.get(sesID);
        setDetectedFullEvent(detectedFullEvent, eventType, time, lastEventType);
        lastestFullEvent.put(sesID, detectedFullEvent);
    }

    private void setDetectedFullEvent(DetectedFullEvent detectedFullEvent, String eventType, Date time, String lastType) {
        switch (eventType) {
            case DIAGNOSIS_TYPE:
                //如果上一个为恢复阶段，则事件结束
                if (RECOVERY_TYPE.equals(lastType)) {
                    detectedFullEvent.setEndTime(time);
                    detectedFullEvent.setRecoveryEndTime(time);
                } else {
                    detectedFullEvent.setDiagnosisStartTime(time);
                    detectedFullEvent.setStartTime(time);
                }
                break;
            case PREPARE_TYPE:
                detectedFullEvent.setPrepareStartTime(time);
                break;
            case RESPONSE_TYPE:
                detectedFullEvent.setResponseStartTime(time);
                break;
            case RECOVERY_TYPE:
                detectedFullEvent.setRecoveryStartTime(time);
                break;
            default:
                try {
                    throw new Exception("错误的数据类型");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void saveTest() {
        DetectedEvent detectedEvent = new DetectedEvent();
        detectedEvent.setStartTimeLong(10L);
        detectedEventDao.saveOrUpdate(detectedEvent);
    }

}
