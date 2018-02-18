package yuan.flood.dao.Entity.UIDTO;

public class SubscribeParamsDTO {
    public SensorSetParamsDTO dataset;
    public EventParamsDTO event;
    public PhaseServiceParamsDTO service;
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

    public PhaseServiceParamsDTO getService() {
        return service;
    }

    public void setService(PhaseServiceParamsDTO service) {
        this.service = service;
    }
}
