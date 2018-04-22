package yuan.flood.mvc;

import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yuan.flood.dao.Entity.DetectedEvent;
import yuan.flood.dao.Entity.PredictArrayResult;
import yuan.flood.dao.Entity.StatisticFloodResult;
import yuan.flood.dao.IDao.IDetectedEventDao;
import yuan.flood.dao.IDao.IPredictWaterLevelResultDao;
import yuan.flood.dao.IDao.IStatisticFloodDao;
import yuan.flood.service.IService.*;
import yuan.flood.service.PreparePhaseService;
import yuan.flood.service.TestService;
import yuan.flood.sos.DataTimeSeries;
import yuan.flood.sos.Decode;
import yuan.flood.sos.Encode;
import yuan.flood.until.HttpMethods;
import yuan.flood.until.SOSSESConfig;

import javax.xml.crypto.Data;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Yuan on 2017/2/11.
 */
@Controller
public class testCo {

    @Autowired
    @Qualifier("testService")
    private IPreparePhaseServiceT testService;
    @Autowired
    @Qualifier("emailTestService")
    private IPreparePhaseServiceT emailTestService;
    @Autowired
    private Encode encode;
    @Autowired
    private Decode decode;
    @Autowired
    private HttpMethods methods;
    @Autowired
    private IDetectedEventDao detectedEventDao;
    @Autowired
    private IPredictWaterLevelResultDao predictWaterLevelResultDao;
    @Autowired
    private IStatisticFloodDao statisticFloodDao;

    @RequestMapping(value = "test")
    public String test() {
        return "test";
    }

    @ResponseBody
    @RequestMapping(value = "/sad", method = RequestMethod.GET)
    public String dataTimeSeries() {
        String res = null;
        res = "[{type:'areaspline',data:[29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]," +
                "pointStart:1420070400000,pointIntervalUnit:'month',zoneAxis:'x',zones:[{value:1433116800000" +
                ",color:'#90ed7d'},{value:1435708800000,color:'#f15c80'},{value:1441065600000,color:'#90ed7d'}]}]";
        return res;
    }


    @CrossOrigin(value = "*")
    @ResponseBody
    @RequestMapping(value = "/rand")
    public Integer getRandom() {
        Random random = new Random();
        return random.nextInt();
    }

    @CrossOrigin(value = "*")
    @ResponseBody
    @RequestMapping(value = "/sadJson")
    public String dataSadTest() {
        String res = "[{\"type\":\"areaspline\",\"data\":[29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]," +
                "\"pointStart\":1420070400000,\"pointIntervalUnit\":\"month\",\"zoneAxis\":\"x\",\"zones\":[{\"value\":1433116800000" +
                ",\"color\":\"#90ed7d\"},{\"value\":1435708800000,\"color\":\"#f15c80\"},{\"value\":1441065600000,\"color\":\"#90ed7d\"}]}]";
        return res;
    }

    @CrossOrigin(value = "*")
    @RequestMapping(value = "/testPrepare")
    public void getTestPrepare() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse("2018-04-12 13:44:54");
            Date date1 = simpleDateFormat.parse("2018-04-12 13:30:42");
            Date date2 = simpleDateFormat.parse("2018-04-12 13:48:54.25");
            for (int i=0;i<10;i++) {
                emailTestService.executeService("Event1", date);
            }
//            testService.executeService("Event1", date);
//            testService.executeService("Event1", date1);
//            testService.executeService("Event1", date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @CrossOrigin(value = "*")
    @RequestMapping(value = "/statisticTime")
    public void getStatisticTime() {
        //先获取现有的传感器数据
        String SensorID = "urn:liesmars:insitusensor:hydrological:hanchuan";
        String propertyID = "urn:ogc:def:property:OGC:1.0:waterLevel";
        String url = SOSSESConfig.getSosurl();
        String observationRequestXML = encode.getGetObservationXML(SensorID, propertyID);
        String responseXML = methods.sendPost(url, observationRequestXML);
        List<DataTimeSeries> dataTimeSeries = null;
        try {
            dataTimeSeries = decode.decodeObservation(responseXML);
        } catch (XmlException e) {
            e.printStackTrace();
        }
        //再获取事件探测结果
        String findStr = "from DetectedEvent d where d.event.eventID='Event1' order by d.startTimeLong";
        List<DetectedEvent> events = detectedEventDao.find(findStr);
        //对齐，并计算时间误差

        List<DataTimeSeries> result = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date dateStart = null;
        Date dignosisEnd = null;
        Date diagnosisSecondStart = null;
        Date diagnosisiSecondEnd = null;
        Date diagnosisThirdStart = null;
        Date diagnosisThirdEnd = null;
        try {
            dateStart = simpleDateFormat.parse("2018-04-12 09:32:04.681");
            dignosisEnd = simpleDateFormat.parse("2018-04-12 13:27:54");
            diagnosisSecondStart = simpleDateFormat.parse("2018-04-12 13:40:51");
            diagnosisiSecondEnd = simpleDateFormat.parse("2018-04-12 13:44:54");
            diagnosisThirdStart = simpleDateFormat.parse("2018-04-12 13:53:53");
            diagnosisiSecondEnd = simpleDateFormat.parse("2018-04-12 14:02:54");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //对齐diagnosis的时间状态
        //计算小于28的时间信息，以及diagnosis的时间点
        Collections.sort(dataTimeSeries);
        Long sumLong = 0l;
        List<DetectedEvent> diagnosisEvent = new ArrayList<>();
        for (DetectedEvent detectedEvent:events){
            if (detectedEvent.getEventTypeName().equals("diagnosis") && detectedEvent.getStartTimeLong() > dateStart.getTime()) {
                diagnosisEvent.add(detectedEvent);
            }
        }
        List<DataTimeSeries> reusltDataTimeSeries = new ArrayList<>();
        for (DataTimeSeries dataTime : dataTimeSeries) {
            if (dataTime.getTimeLon() > dateStart.getTime() && dataTime.getDataValue() < 28) {
                reusltDataTimeSeries.add(dataTime);
                sumLong = sumLong + getMinTimeLen(dataTime, diagnosisEvent);
            }
        }

        //计算诊断阶段时间
        Long averageTime = sumLong / reusltDataTimeSeries.size();
        Long serviceTime = getMessageToServiceTime();
        System.out.println(averageTime + "," + serviceTime);

    }

    /**
     * 计算从消息到服务的平均时间
     */
    public Long getMessageToServiceTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date dateStart = null;
        try {
            dateStart = simpleDateFormat.parse("2018-04-12 09:32:04.681");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取预测的时间信息
        String findStr1 = "from PredictArrayResult p where p.subscibeEventParams.eventID='Event1' order by p.predictTime";
        List<PredictArrayResult> predictArrayResultList = predictWaterLevelResultDao.find(findStr1);
        //统计时间
        String findStr2="from StatisticFloodResult p where p.subscibeEventParams.eventID='Event1' order by p.statisticTime";
        List<StatisticFloodResult> statisticFloodResultList = statisticFloodDao.find(findStr2);

        //再获取事件探测结果
        String findStr = "from DetectedEvent d where d.event.eventID='Event1' order by d.startTimeLong";
        List<DetectedEvent> events = detectedEventDao.find(findStr);
        List<DetectedEvent> detectedEvents = new ArrayList<>();
        for (DetectedEvent detectedEvent : events) {
            if (!detectedEvent.getEventTypeName().equals("diagnosis") && detectedEvent.getEndTimeLong() > dateStart.getTime()) {
                detectedEvents.add(detectedEvent);
            }
        }
        //找最近的时间同类型的

        //准备阶段的
        Long sumLong = 0L;
        int count=0;
        PredictArrayResult lastPredict = null;
        for (PredictArrayResult predictArrayResult : predictArrayResultList) {
            if (lastPredict==null||!lastPredict.getSubject().equals(predictArrayResult.getSubject())){
                if (predictArrayResult.getSubject().equals("准备阶段")){
                    sumLong = sumLong + getMinTimeLen(predictArrayResult.getPredictTime(), detectedEvents, "prepare");
                    count++;
                }else {
                    sumLong = sumLong + getMinTimeLen(predictArrayResult.getPredictTime(), detectedEvents, "response");
                    count++;
                }
            }
            lastPredict = predictArrayResult;

        }
        //处理recovery时候内容
        for (StatisticFloodResult statisticFloodResult : statisticFloodResultList) {
            sumLong = sumLong + getMinTimeLen(statisticFloodResult.getStatisticTime(), detectedEvents, "recovery");
            count++;
        }
        //计算平均耗时
        Long avg = sumLong / count;
        return avg;
    }


    public Long getMinTimeLen(DataTimeSeries dataTimeSeries, List<DetectedEvent> events) {
        Long result = 0L;
        DetectedEvent detectedEvent1 = null;
        for (DetectedEvent detectedEvent : events) {
            if (detectedEvent.getEndTimeLong() > dataTimeSeries.getTimeLon()) {
                detectedEvent1 = detectedEvent;
                break;
            }
        }
        result=  detectedEvent1.getEndTimeLong() - dataTimeSeries.getTimeLon();
        events.remove(detectedEvent1);
        return result;
    }

    public Long getMinTimeLen(Date time, List<DetectedEvent> events, String Type) {
        DetectedEvent detectedEvent = null;
            for (DetectedEvent event : events) {
                if (event.getEventTypeName().equals(Type)&&time.getTime()>event.getEndTimeLong()) {
                    detectedEvent = event;
                    break;
                }
            }
         Long time1 = time.getTime() - detectedEvent.getEndTimeLong();
        events.remove(detectedEvent);
        return time1;
    }

}
