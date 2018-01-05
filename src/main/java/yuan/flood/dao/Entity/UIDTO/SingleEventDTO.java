package yuan.flood.dao.Entity.UIDTO;

import yuan.flood.dao.Entity.UIEntity.SubscribeEventParamsDTO;

public class SingleEventDTO {
    private SingleEventSensorDTO sensors;
    private SubscribeEventParamsDTO params;

    public SingleEventSensorDTO getSensors() {
        return sensors;
    }

    public void setSensors(SingleEventSensorDTO sensors) {
        this.sensors = sensors;
    }

    public SubscribeEventParamsDTO getParams() {
        return params;
    }

    public void setParams(SubscribeEventParamsDTO params) {
        this.params = params;
    }
}
