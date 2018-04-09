package yuan.flood.service;

import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.DetectedFullEvent;
import yuan.flood.dao.Entity.StatisticFloodResult;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.IDao.IDetectedFullEventDao;
import yuan.flood.dao.IDao.IStatisticFloodDao;
import yuan.flood.service.IService.IEventService;
import yuan.flood.service.IService.IPhaseService;
import yuan.flood.service.IService.IRecoveryPhaseService;
import yuan.flood.service.IService.ISensorService;
import yuan.flood.sos.DataTimeSeries;
import yuan.flood.sos.Decode;
import yuan.flood.sos.Encode;
import yuan.flood.until.HttpMethods;
import yuan.flood.until.SOSSESConfig;

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
                Date maxWaterDate = simpleDateFormat.parse(dataTimeSeries.get(maxWaterLevelKey).getTimeStr());
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

        //存放在数据库中
        statisticFloodDao.save(statisticFloodResult);

    }
}
