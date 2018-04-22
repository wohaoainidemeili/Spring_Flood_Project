package yuan.flood.service;

import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.AlertFloodResult;
import yuan.flood.dao.Entity.DetectedFullEvent;
import yuan.flood.dao.Entity.StatisticFloodResult;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.IDao.IAlertFloodDao;
import yuan.flood.dao.IDao.IDetectedFullEventDao;
import yuan.flood.dao.IDao.IStatisticFloodDao;
import yuan.flood.service.IService.IEventService;
import yuan.flood.service.IService.IPhaseService;
import yuan.flood.service.IService.IRecoveryPhaseService;
import yuan.flood.service.IService.ISensorService;
import yuan.flood.service.function.SendMail;
import yuan.flood.sos.DataTimeSeries;
import yuan.flood.sos.Decode;
import yuan.flood.sos.Encode;
import yuan.flood.until.HttpMethods;
import yuan.flood.until.SOSSESConfig;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Service
public class RecoveryPhaseService implements IRecoveryPhaseService {
    @Autowired
    IEventService eventService;
    @Autowired
    ISensorService sensorService;
    @Autowired
    private Encode encode;
    @Autowired
    private HttpMethods methods;
    @Autowired
    private Decode decode;
    @Autowired
    private IStatisticFloodDao statisticFloodDao;
    @Autowired
    private IDetectedFullEventDao detectedFullEventDao;
    @Autowired
    private IAlertFloodDao alertFloodDao;
    @Override
    public void executeService(String sesID, Date date,DetectedFullEvent object) {
        //获取sesID
        SubscibeEventParams subscibeEventParams = eventService.getRegisteredEventParamsBySesid(sesID);
        // /计算最大水位值，计算水位时间等
        StatisticFloodResult statisticFloodResult = new StatisticFloodResult();

        DetectedFullEvent fullEvent = object;
        //根据传感器ID和属性ID来获取数据
        String observationID = subscibeEventParams.getResponseObservation();
        String sensorID = subscibeEventParams.getResponseSensor();
        String observationRequestXML = encode.getGetObservationByTimeXML(sensorID, observationID, fullEvent.getStartTime(), fullEvent.getEndTime());
        String responseXML= methods.sendPost(SOSSESConfig.getSosurl(), observationRequestXML);
        try {
            List<DataTimeSeries> dataTimeSeries= decode.decodeObservation(responseXML);
           //寻找最大水位值
            Double maxWaterLevel = -Double.MAX_VALUE;
            int maxWaterLevelKey = 0;
            for (int i=0;i<dataTimeSeries.size();i++) {
                Double currentWaterLevel = dataTimeSeries.get(i).getDataValue();
                if (maxWaterLevel <currentWaterLevel ) {
                    maxWaterLevel = currentWaterLevel;
                    maxWaterLevelKey = i;
                }
            }
            statisticFloodResult.setMaxWaterLevel(maxWaterLevel);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            try {
                Date maxWaterDate = simpleDateFormat.parse(dataTimeSeries.get(maxWaterLevelKey).getTimeStr().replace("+08:00", "+0800"));
                statisticFloodResult.setMaxWaterLevelTime(maxWaterDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (XmlException e) {
            e.printStackTrace();
        }
        //赋值
        statisticFloodResult.setStartTime(object.getStartTime());
        statisticFloodResult.setEndTime(object.getEndTime());
        statisticFloodResult.setDiagnosisStartTime(object.getDiagnosisStartTime());
        statisticFloodResult.setPrepareStartTime(object.getPrepareStartTime());
        statisticFloodResult.setResponseStartTime(object.getResponseStartTime());
        statisticFloodResult.setRecoveryStartTime(object.getRecoveryStartTime());
        statisticFloodResult.setRecoveryEndTime(object.getRecoveryEndTime());

        //计算准备阶段时间
        Long prepareTimeLength = fullEvent.getResponseStartTime().getTime() - fullEvent.getPrepareStartTime().getTime();
        Long responnseTimeLength = fullEvent.getRecoveryStartTime().getTime() - fullEvent.getResponseStartTime().getTime();
        Long recoveryTimeLength = fullEvent.getRecoveryEndTime().getTime() - fullEvent.getRecoveryStartTime().getTime();

        statisticFloodResult.setPrepareDuration(prepareTimeLength);
        statisticFloodResult.setResponseDuration(responnseTimeLength);
        statisticFloodResult.setRecoveryDuration(recoveryTimeLength);
        statisticFloodResult.setStatisticTime(new Date());
        statisticFloodResult.setSubscibeEventParams(subscibeEventParams);
        //设置全事件的关联事件信息，并存储
        object.setEvent(subscibeEventParams);
        detectedFullEventDao.save(object);

        //发送消息，说明事件已经回退到诊断阶段
        AlertFloodResult alertFloodResult = new AlertFloodResult();
        alertFloodResult.setTime(new Date());
        alertFloodResult.setSubscibeEventParams(subscibeEventParams);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String dateStr = simpleDateFormat.format(date);
        try {
            //create message
            StringBuffer message = new StringBuffer();
            message.append("您订阅的事件\"" + subscibeEventParams.getUserDefineName() + "\"于" + dateStr + "水位恢复至正常。\r\n");
            alertFloodResult.setSubject(subscibeEventParams.getUserDefineName() + "事件水位恢复至正常");
            //email发送消息内容
            SendMail.send("wenying3413ying@126.com", "dwytam1314", subscibeEventParams.getEmail(), subscibeEventParams.getUserDefineName() + "事件水位恢复至正常", message.toString());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //发送完数据，将信息写入
        alertFloodDao.save(alertFloodResult);

        //存放在数据库中
        statisticFloodDao.save(statisticFloodResult);

    }
}
