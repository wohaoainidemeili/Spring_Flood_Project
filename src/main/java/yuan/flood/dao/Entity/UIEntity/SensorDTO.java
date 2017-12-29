package yuan.flood.dao.Entity.UIEntity;

import yuan.flood.dao.Entity.ObservedProperty;

import java.util.*;

public class SensorDTO {
    private String sensorID;
    private String sensorName;
    private double lat;
    private double lon;
    private List<ObservedPropertyDTO> observedProperties=new ArrayList<ObservedPropertyDTO>();

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

    public List<ObservedPropertyDTO> getObservedProperties() {
        return observedProperties;
    }

    public void setObservedProperties(List<ObservedPropertyDTO> observedProperties) {
        this.observedProperties = observedProperties;
    }
}
