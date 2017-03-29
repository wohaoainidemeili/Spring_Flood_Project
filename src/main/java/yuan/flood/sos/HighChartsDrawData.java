package yuan.flood.sos;

import yuan.flood.dao.Entity.DetectedEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuan on 2017/3/17.
 */
public class HighChartsDrawData {
    private List<DataTimeSeries> timeAndValue=new ArrayList<DataTimeSeries>();
    private List<DetectedEvent> eventTypeAndTime=new ArrayList<DetectedEvent>();
    private Long startPoint;

    public List<DataTimeSeries> getTimeAndValue() {
        return timeAndValue;
    }

    public void setTimeAndValue(List<DataTimeSeries> timeAndValue) {
        this.timeAndValue = timeAndValue;
    }

    public List<DetectedEvent> getEventTypeAndTime() {
        return eventTypeAndTime;
    }

    public void setEventTypeAndTime(List<DetectedEvent> eventTypeAndTime) {
        this.eventTypeAndTime = eventTypeAndTime;
    }

    public Long getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Long startPoint) {
        this.startPoint = startPoint;
    }
}
