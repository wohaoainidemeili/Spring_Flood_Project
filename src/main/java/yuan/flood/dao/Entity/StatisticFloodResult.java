package yuan.flood.dao.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 洪涝统计结果表存储
 */
public class StatisticFloodResult implements Serializable {
    private Long id;
    private Date startTime;
    private Date endTime;
    private Date diagnosisStartTime;
    private Date prepareStartTime;
    private Date responseStartTime;
    private Date recoveryStartTime;
    private Date recoveryEndTime;
    private Date statisticTime;
    private Double maxWaterLevel;
    private Date maxWaterLevelTime;
    private Long prepareDuration;
    private Long responseDuration;
    private Long recoveryDuration;
    private SubscibeEventParams subscibeEventParams;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStatisticTime() {
        return statisticTime;
    }

    public void setStatisticTime(Date statisticTime) {
        this.statisticTime = statisticTime;
    }

    public Double getMaxWaterLevel() {
        return maxWaterLevel;
    }

    public void setMaxWaterLevel(Double maxWaterLevel) {
        this.maxWaterLevel = maxWaterLevel;
    }

    public Long getPrepareDuration() {
        return prepareDuration;
    }

    public void setPrepareDuration(Long prepareDuration) {
        this.prepareDuration = prepareDuration;
    }

    public Long getResponseDuration() {
        return responseDuration;
    }

    public void setResponseDuration(Long responseDuration) {
        this.responseDuration = responseDuration;
    }

    public Long getRecoveryDuration() {
        return recoveryDuration;
    }

    public void setRecoveryDuration(Long recoveryDuration) {
        this.recoveryDuration = recoveryDuration;
    }

    public SubscibeEventParams getSubscibeEventParams() {
        return subscibeEventParams;
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

    public void setSubscibeEventParams(SubscibeEventParams subscibeEventParams) {
        this.subscibeEventParams = subscibeEventParams;
    }

    public Date getMaxWaterLevelTime() {
        return maxWaterLevelTime;
    }

    public void setMaxWaterLevelTime(Date maxWaterLevelTime) {
        this.maxWaterLevelTime = maxWaterLevelTime;
    }
}
