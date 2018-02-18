package yuan.flood.sos;

/**
 * Created by Yuan on 2017/3/1.
 */
public class DataTimeSeries implements Comparable<DataTimeSeries>{

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

    @Override
    public int compareTo(DataTimeSeries o) {
        if (this.timeLon>o.timeLon) return 1;
        else if (this.timeLon<o.timeLon) return -1;
        else return 0;
    }
}
