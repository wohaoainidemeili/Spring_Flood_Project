package yuan.flood.dao.Entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Yuan on 2017/1/15.
 */
public class Sensor {
    private String sensorID;
    private String sensorName;
    private double lat;
    private double lon;
    private Set<ObservedProperty> observedProperties=new HashSet<ObservedProperty>();

    public String getSensorID() {
        return sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Set<ObservedProperty> getObservedProperties() {
        return observedProperties;
    }

    public void setObservedProperties(Set<ObservedProperty> observedProperties) {
        this.observedProperties = observedProperties;
    }
}
