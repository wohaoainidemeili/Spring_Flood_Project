package yuan.flood.service;

import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.ObservedProperty;
import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.Entity.SensorObsProperty;
import yuan.flood.dao.IDao.IObsPropertyDao;
import yuan.flood.dao.IDao.ISensorDao;
import yuan.flood.dao.IDao.ISensorObsPropertyDao;
import yuan.flood.service.IService.ISensorService;
import yuan.flood.sos.DataTimeSeries;
import yuan.flood.sos.Decode;
import yuan.flood.sos.Encode;
import yuan.flood.until.HttpMethods;
import yuan.flood.until.ReadConfig;
import yuan.flood.until.SOSSESConfig;

import java.util.*;

/**
 * Created by Yuan on 2017/1/16.
 */
@Service
public class SensorService implements ISensorService {
    @Autowired
    private Decode sosDecode;
    @Autowired
    private Encode sosEncode;
    @Autowired
    private HttpMethods methods;
    @Autowired
    private ISensorDao sensorDao;
    @Autowired
    private IObsPropertyDao obsPropertyDao;
    @Autowired
    private ISensorObsPropertyDao sensorObsPropertyDao;

    /**
     * get sensors when open the show sensor page
     * @param url the sos url
     * @return
     */
    @Override
    public List<Sensor> getSensors(String url) {
        String sosCapabilityXML= sosEncode.getCapability();
        String capabilityResponse= methods.sendPost(url, sosCapabilityXML);
        List<String> sensorIDs=null;
        try {
             sensorIDs=sosDecode.decodeCapability(capabilityResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Sensor> sensors=new ArrayList<Sensor>();
        for (String sensorID:sensorIDs){
            String describeSensorXML= sosEncode.getDescribeSensorXML(sensorID);
            String decribeSensorResponseXML=methods.sendPost(url,describeSensorXML);
            try {
               Sensor sensor= sosDecode.decodeDescribeSensor(decribeSensorResponseXML);
                sensors.add(sensor);
            } catch (XmlException e) {
                e.printStackTrace();
            }
        }
        return sensors;
    }

    @Override
    public void saveSensorsAndObsProperty(List<Sensor> sensors) {

        //已经
        List<String> hasSavedPropertyID = new ArrayList<>();

        try {
            for (Sensor sensor:sensors) {
                if (isSavedProperty(sensor,hasSavedPropertyID))
                    sensorDao.merge(sensor);
                else {
                    sensorDao.saveOrUpdate(sensor);
                    for (ObservedProperty observedProperty : sensor.getObservedProperties()) {
                        hasSavedPropertyID.add(observedProperty.getPropertyID());
                    }
                }
            }

            //存储中间表，获得每个传感器的所有属性
            List<String> hasSavedSensorPropertyIDs = new ArrayList<>();
            for (Sensor sensor : sensors) {
                if (sensor.getObservedProperties()!=null)
                    for (ObservedProperty property:sensor.getObservedProperties())
                    {
                        SensorObsProperty sensorObsProperty = new SensorObsProperty();
                        sensorObsProperty.setSensorID(sensor.getSensorID());
                        sensorObsProperty.setObservedPropertyID(property.getPropertyID());
                        sensorObsProperty.setSensorName(sensor.getSensorName());
                        sensorObsProperty.setPropertyName(property.getPropertyName());

                        //找表中是否存在这个属性数据
                        if (!getIsSensorProperty(sensor.getSensorID(), property.getPropertyID())) {
                            Long Id = getMaxPropertyID();
                            sensorObsProperty.setId(Id);
                            sensorObsPropertyDao.saveOrupdate(sensorObsProperty);
                        }

                    }
            }
        }catch (Exception e){

        }

    }

    public boolean isSavedProperty(Sensor sensor,List<String> hasSavedObsID) {
        for (ObservedProperty observedProperty : sensor.getObservedProperties()) {
            if (hasSavedObsID.contains(observedProperty.getPropertyID())) {
                return true;
            }
        }
        return false;
    }

    public Long getMaxPropertyID() {
        String findCountStr="select count(*) from SensorObsProperty s";
        String findMaxOrderStr="select max(s.id) from SensorObsProperty s";
        List<Long> count=sensorObsPropertyDao.find(findCountStr);
        Long countN=count.get(0);
        if (countN==0){
            return 0L;
        }else {
            List<Long> maxorder= sensorObsPropertyDao.find(findMaxOrderStr);
            return maxorder.get(0)+1;
        }
    }
    public boolean getIsSensorProperty(String sensorID, String propertyID) {
        String countHql = "select count(*) from SensorObsProperty s";
        String hql = "from SensorObsProperty s where s.sensorID='" + sensorID + "' and s.observedPropertyID='" + propertyID + "'";
        List<Long> countList = sensorObsPropertyDao.find(countHql);
        Long count = countList.get(0);
        if (count==0)
            return false;
        try {
            List result = sensorObsPropertyDao.find(hql);
            if (result == null || result.isEmpty()) {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }

        return true;
    }
   public List<Sensor> findObseredPropertyBySensorID(String sensorID){
       String hql="from Sensor s where s.sensorID='"+sensorID+"'";
       List<Sensor> result=sensorDao.find(hql);
       return result;
   }

    @Override
    public List<DataTimeSeries> getSensorDataByTime(String sensorID, String observationID, Date startTime, Date endTime) {
        List<DataTimeSeries> dataTimeSeries = null;
        Encode encode = new Encode();
        Decode decode = new Decode();
        String dataRequestXML = encode.getGetObservationByTimeXML(sensorID, observationID, startTime, endTime);
        try {
           dataTimeSeries= decode.decodeObservation(dataRequestXML);
        } catch (XmlException e) {
            e.printStackTrace();
        }

        return dataTimeSeries;
    }

    @Override
    public DataTimeSeries getLatestSensorData(String sensorID, String observationID) {
        List<DataTimeSeries> dataTimeSeries = null;
        Encode encode = new Encode();
        Decode decode = new Decode();
        String dataRequestXML = encode.getGetLatestObservationXML(sensorID, observationID);

        try {
            dataTimeSeries = decode.decodeObservation(dataRequestXML);
        } catch (XmlException e) {
            e.printStackTrace();
        }
        if (dataTimeSeries==null||dataTimeSeries.isEmpty()) return null;
        else return dataTimeSeries.get(0);
    }
}
