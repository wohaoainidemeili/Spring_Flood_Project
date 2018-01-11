package yuan.flood.dao.Entity.UIDTO;

import yuan.flood.dao.Entity.UIEntity.SensorDTO;
import yuan.flood.dao.Entity.UIEntity.SubscribeEventParamsDTO;

import java.util.List;

public class SingleEventDTO {
    private SingleEventSensorDTO dataset;
    private SubscribeEventParamsDTO params;
    private List<SensorDTO> sensors;
    private List<EventSensorPropertyDTO> propertys;

    public SingleEventSensorDTO getDataset() {
        return dataset;
    }

    public void setDataset(SingleEventSensorDTO dataset) {
        this.dataset = dataset;
    }

    public List<SensorDTO> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorDTO> sensors) {
        this.sensors = sensors;
    }

    public SubscribeEventParamsDTO getParams() {
        return params;
    }

    public void setParams(SubscribeEventParamsDTO params) {
        this.params = params;
    }

    public List<EventSensorPropertyDTO> getPropertys() {
        return propertys;
    }

    public void setPropertys(List<EventSensorPropertyDTO> propertys) {
        this.propertys = propertys;
    }
}
