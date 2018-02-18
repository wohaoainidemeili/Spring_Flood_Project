package yuan.flood.dao.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 记录一次事件
 */
public class DetectedFullEvent implements Serializable {
    private Long id;
    /**
     * 这次事件的开始时间点
     */
    private Date startTime;
    /**
     * 这次事件的结束时间点
     */
    private Date endTime;

    private Date diagnosisStartTime;
    private Date prepareStartTime;
    private Date responseStartTime;
    private Date recoveryStartTime;
    private Date recoveryEndTime;

    private SubscibeEventParams event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getDiagnosisStartTime() {
        return diagnosisStartTime;
    }

    public void setDiagnosisStartTime(Date diagnosisStartTime) {
        this.diagnosisStartTime = diagnosisStartTime;
    }

    public Date getPrepareStartTime() {
        return prepareStartTime;
    }

    public void setPrepareStartTime(Date prepareStartTime) {
        this.prepareStartTime = prepareStartTime;
    }

    public Date getResponseStartTime() {
        return responseStartTime;
    }

    public void setResponseStartTime(Date responseStartTime) {
        this.responseStartTime = responseStartTime;
    }

    public Date getRecoveryStartTime() {
        return recoveryStartTime;
    }

    public void setRecoveryStartTime(Date recoveryStartTime) {
        this.recoveryStartTime = recoveryStartTime;
    }

    public Date getRecoveryEndTime() {
        return recoveryEndTime;
    }

    public void setRecoveryEndTime(Date recoveryEndTime) {
        this.recoveryEndTime = recoveryEndTime;
    }

    public SubscibeEventParams getEvent() {
        return event;
    }

    public void setEvent(SubscibeEventParams event) {
        this.event = event;
    }
}
