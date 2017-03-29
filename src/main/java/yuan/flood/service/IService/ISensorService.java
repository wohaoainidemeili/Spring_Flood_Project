package yuan.flood.service.IService;

import yuan.flood.dao.Entity.ObservedProperty;
import yuan.flood.dao.Entity.Sensor;

import java.util.List;

/**
 * Created by Yuan on 2017/1/16.
 */
public interface ISensorService {
    public List<Sensor> getSensors(String url);
    public void saveSensorsAndObsProperty(List<Sensor> sensors);
    public List<Sensor> findObseredPropertyBySensorID(String sensorID);
}
