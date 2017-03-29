package yuan.flood.sos;

/**
 * Created by Yuan on 2017/3/1.
 */
public class DataTimeSeries {

    private String eventType;
    private Double dataValue;
    private String timeStr;
    private Long timeLon;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Double getDataValue() {
        return dataValue;
    }

    public void setDataValue(Double dataValue) {
        this.dataValue = dataValue;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public Long getTimeLon() {
        return timeLon;
    }

    public void setTimeLon(Long timeLon) {
        this.timeLon = timeLon;
    }
}
