package yuan.flood.dao.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Yuan on 2017/2/15.
 */
public class DetectedEvent implements Serializable {
    private Long id;
    private String eventTypeName;
    private Date startTime;
    private Date endTime;
    private Long startTimeLong;
    private Long endTimeLong;
    private SubscibeEventParams event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getStartTimeLong() {
        return startTimeLong;
    }

    public void setStartTimeLong(Long startTimeLong) {
        this.startTimeLong = startTimeLong;
    }

    public Long getEndTimeLong() {
        return endTimeLong;
    }

    public void setEndTimeLong(Long endTimeLong) {
        this.endTimeLong = endTimeLong;
    }

    public SubscibeEventParams getEvent() {
        return event;
    }

    public void setEvent(SubscibeEventParams event) {
        this.event = event;
    }
}
