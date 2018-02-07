package yuan.flood.dao.Entity;

import java.io.Serializable;
import java.util.Set;

/**
 * 传感器与属性的中间表
 */
public class SensorObsProperty implements Serializable{
    private Long id;
    private String sensorID;
    private String observedPropertyID;
    private String sensorName;
    private String propertyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorID() {
        return sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    public String getObservedPropertyID() {
        return observedPropertyID;
    }

    public void setObservedPropertyID(String observedPropertyID) {
        this.observedPropertyID = observedPropertyID;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
