package yuan.flood.phase;

import org.springframework.beans.factory.annotation.Autowired;
import yuan.flood.dao.IDao.IAlertFloodDao;
import yuan.flood.dao.IDao.IDetectedFullEventDao;
import yuan.flood.dao.IDao.IPredictWaterLevelResultDao;
import yuan.flood.dao.IDao.IStatisticFloodDao;
import yuan.flood.service.IService.IEventService;
import yuan.flood.service.IService.ISensorObsPropertyService;
import yuan.flood.service.IService.ISensorService;
import yuan.flood.sos.Decode;
import yuan.flood.sos.Encode;
import yuan.flood.until.HttpMethods;

import java.util.Date;

public abstract class AbastractPhaseService implements IPhaseService {
    IEventService eventService;
    ISensorService sensorService;
    ISensorObsPropertyService sensorObsPropertyService;
    private Encode encode;
    private HttpMethods methods;
    private Decode decode;
    private IPredictWaterLevelResultDao predictWaterLevelResultDao;
    private IAlertFloodDao alertFloodDao;
    private IStatisticFloodDao statisticFloodDao;
    private IDetectedFullEventDao detectedFullEventDao;

    abstract void executeService(String sesID, Date date);

    public IEventService getEventService() {
        return eventService;
    }

    public void setEventService(IEventService eventService) {
        this.eventService = eventService;
    }

    public ISensorService getSensorService() {
        return sensorService;
    }

    public void setSensorService(ISensorService sensorService) {
        this.sensorService = sensorService;
    }

    public ISensorObsPropertyService getSensorObsPropertyService() {
        return sensorObsPropertyService;
    }

    public void setSensorObsPropertyService(ISensorObsPropertyService sensorObsPropertyService) {
        this.sensorObsPropertyService = sensorObsPropertyService;
    }

    public Encode getEncode() {
        return encode;
    }

    public void setEncode(Encode encode) {
        this.encode = encode;
    }

    public HttpMethods getMethods() {
        return methods;
    }

    public void setMethods(HttpMethods methods) {
        this.methods = methods;
    }

    public Decode getDecode() {
        return decode;
    }

    public void setDecode(Decode decode) {
        this.decode = decode;
    }

    public IPredictWaterLevelResultDao getPredictWaterLevelResultDao() {
        return predictWaterLevelResultDao;
    }

    public void setPredictWaterLevelResultDao(IPredictWaterLevelResultDao predictWaterLevelResultDao) {
        this.predictWaterLevelResultDao = predictWaterLevelResultDao;
    }

    public IAlertFloodDao getAlertFloodDao() {
        return alertFloodDao;
    }

    public void setAlertFloodDao(IAlertFloodDao alertFloodDao) {
        this.alertFloodDao = alertFloodDao;
    }

    public IStatisticFloodDao getStatisticFloodDao() {
        return statisticFloodDao;
    }

    public void setStatisticFloodDao(IStatisticFloodDao statisticFloodDao) {
        this.statisticFloodDao = statisticFloodDao;
    }

    public IDetectedFullEventDao getDetectedFullEventDao() {
        return detectedFullEventDao;
    }

    public void setDetectedFullEventDao(IDetectedFullEventDao detectedFullEventDao) {
        this.detectedFullEventDao = detectedFullEventDao;
    }
}
