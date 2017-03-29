package yuan.flood.service;

import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.ObservedProperty;
import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.IDao.IObsPropertyDao;
import yuan.flood.dao.IDao.ISensorDao;
import yuan.flood.service.IService.ISensorService;
import yuan.flood.sos.Decode;
import yuan.flood.sos.Encode;
import yuan.flood.until.HttpMethods;
import yuan.flood.until.ReadConfig;
import yuan.flood.until.SOSSESConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

        try {
//            Sensor sensor=new Sensor();
//            sensor.setSensorID("sadas");
//            sensor.setObservedProperties(new HashSet<ObservedProperty>());
//            ObservedProperty observedProperty=new ObservedProperty();
//            observedProperty.setPropertyID("obs");
//            observedProperty.setSensors(new HashSet<Sensor>());
//            sensor.getObservedProperties().add(observedProperty);
//            observedProperty.getSensors().add(sensor);
            for (Sensor sensor:sensors) {
                sensorDao.saveOrUpdate(sensor);
            }
//            sensorDao.save(sensor);
//            obsPropertyDao.save(observedProperty);
        }catch (Exception e){

        }

    }

   public List<Sensor> findObseredPropertyBySensorID(String sensorID){
       String hql="from Sensor s where s.sensorID='"+sensorID+"'";
       List<Sensor> result=sensorDao.find(hql);
       return result;
   }
}
