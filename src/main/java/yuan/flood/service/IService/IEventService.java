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

    /**
     * 根据eventID构建事件相关传感器的数据内容
     * @param url
     * @param sesid
     * @return
     */
    public String getDetectedEventBySESID(String url,String sesid);

    /**
     * 根据eventID查询事件
     * @param sesid
     * @return
     */
    public SubscibeEventParams getRegisteredEventParamsBySesid(String sesid);

    /**
     * 根据在SES中注册的事件ID查询结果
     * @param url
     * @param sesid
     * @return
     */
    public String getDetectedDataInJsonBySESID(String url, String sesid);

    /**
     * 根据事件ID（在SES中注册的事件ID）、传感器ID以及属性ID构建事件过程JSON数据
     * @param url
     * @param sesid
     * @param sensorID
     * @param propertyID
     * @return
     */
    public String getSingleDataInJsonByEventSensorProperty(String url, String sesid, String sensorID, String propertyID);

    /**
     * 根据在ses中注册的事件ID查询事件
     * @param eventSesID
     * @return
     */
    public SubscibeEventParams getRegisteredEventParamsByEventSesID(String eventSesID);

    /**
     * 获取所有已经注册的事件
     * @return
     */
    public List<SubscibeEventParams> getAllRegisteredEvent();

    public String getLatestEventType(String sesID);

    public List<SubscibeEventParams> getRegisteredEventByUserID(Long userLonID);

}
