package yuan.flood.service.IService;

import yuan.flood.dao.Entity.DetectedEvent;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.sos.DataTimeSeries;
import yuan.flood.sos.HighChartsDrawData;

import java.util.List;
import java.util.Map;

/**
 * Created by Yuan on 2017/2/16.
 */
public interface IEventService {
    public List<String> getAllEventIDs();
    public void saveSubscribeEvent(SubscibeEventParams subscibeEventParams);
    public void saveDetectedEvent(DetectedEvent detectedEvent);
    public Long getMaxEventOrder();
    public String getDetectedEventBySESID(String url,String sesid);
    public SubscibeEventParams getRegisteredEventParamsBySesid(String sesid);
}
