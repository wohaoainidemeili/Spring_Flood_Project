package yuan.flood.service.IService;

import yuan.flood.dao.Entity.SensorObsProperty;

import java.util.List;

public interface ISensorObsPropertyService {
    //输入多个id，找到对应的属性
    public List getSensorPropertyList(List<Long> ids);

    public SensorObsProperty getSensorPropertyByID(Long id);

    public SensorObsProperty getSensorPropertyIDBySensor(SensorObsProperty sensorObsProperty);

    public boolean getIsSensorProperty(String sensorID, String propertyID);
}
