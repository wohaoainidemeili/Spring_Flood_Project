package yuan.flood.dao.Entity.UIDTO;

public class SubscribeParamsDTO {
    public SensorSetParamsDTO dataset;
    public EventParamsDTO event;

    public SensorSetParamsDTO getDataset() {
        return dataset;
    }

    public void setDataset(SensorSetParamsDTO dataset) {
        this.dataset = dataset;
    }

    public EventParamsDTO getEvent() {
        return event;
    }

    public void setEvent(EventParamsDTO event) {
        this.event = event;
    }
}
