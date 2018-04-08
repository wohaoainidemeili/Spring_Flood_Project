package yuan.flood.phase;

import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import yuan.flood.dao.Entity.DetectedFullEvent;
import yuan.flood.dao.Entity.StatisticFloodResult;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.IDao.IStatisticFloodDao;
import yuan.flood.service.IService.IEventService;
import yuan.flood.service.IService.ISensorService;
import yuan.flood.sos.DataTimeSeries;
import yuan.flood.sos.Decode;
import yuan.flood.sos.Encode;
import yuan.flood.until.HttpMethods;
import yuan.flood.until.SOSSESConfig;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class RecoveryPhaseService implements IPhaseService {
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

    @Override
    public void executeService(String sesID, Date date, Object object) {
        //获取sesID
        SubscibeEventParams subscibeEventParams = eventService.getRegisteredEventParamsByEventSesID(sesID);
        // /计算最大水位值，计算水位时间等
        StatisticFloodResult statisticFloodResult = new StatisticFloodResult();

        DetectedFullEvent fullEvent = (DetectedFullEvent) object;
        //根据传感器ID和属性ID来获取数据
        String observationID = subscibeEventParams.getResponseObservation();
        String sensorID = subscibeEventParams.getResponseSensor();
        String observationRequestXML = encode.getGetObservationByTimeXML(sensorID, observationID, fullEvent.getStartTime(), fullEvent.getEndTime());
        String responseXML = methods.sendPost(SOSSESConfig.getSosurl(), observationRequestXML);
        try {
            List<DataTimeSeries> dataTimeSeries = decode.decodeObservation(responseXML);
            //寻找最大水位值
            Double maxWaterLevel = -Double.MAX_VALUE;
            for (int i = 0; i < dataTimeSeries.size(); i++) {
                Double currentWaterLevel = dataTimeSeries.get(i).getDataValue();
                if (maxWaterLevel < currentWaterLevel) {
                    maxWaterLevel = currentWaterLevel;
                }
            }
            statisticFloodResult.setMaxWaterLevel(maxWaterLevel);

        } catch (XmlException e) {
            e.printStackTrace();
        }
        //计算准备阶段时间
        Long prepareTimeLength = fullEvent.getResponseStartTime().getTime() - fullEvent.getPrepareStartTime().getTime();
        Long responnseTimeLength = fullEvent.getRecoveryStartTime().getTime() - fullEvent.getResponseStartTime().getTime();

        statisticFloodResult.setPrepareDuration(prepareTimeLength);
        statisticFloodResult.setResponseDuration(responnseTimeLength);
        statisticFloodResult.setSubscibeEventParams(subscibeEventParams);

        //存放在数据库中
        statisticFloodDao.save(statisticFloodResult);

    }
}
