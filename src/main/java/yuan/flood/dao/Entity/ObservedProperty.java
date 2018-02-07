package yuan.flood.dao.Entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Yuan on 2017/1/15.
 */
public class ObservedProperty implements Serializable {

    private String propertyID;
    private String propertyName;
    private String unit;
    private Set<Sensor> sensors=new HashSet<Sensor>();
    //1对多的
    private Set<SensorObsProperty> sensorObsProperties = new HashSet<>();

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    public Set<SensorObsProperty> getSensorObsProperties() {
        return sensorObsProperties;
    }

    public void setSensorObsProperties(Set<SensorObsProperty> sensorObsProperties) {
        this.sensorObsProperties = sensorObsProperties;
    }
}
