package yuan.flood.dao.Entity.UIEntity;

import java.util.Date;

public class StatisticFloodResultDTO {

    private int key;
    private String startTime;
    private String endTime;
    private String diagnosisStartTime;
    private String prepareStartTime;
    private String responseStartTime;
    private String recoveryStartTime;
    private String recoveryEndTime;
    private String statisticTime;
    private Double maxWaterLevel;
    private String maxWaterLevelTime;
    private Double prepareDuration;
    private Double responseDuration;
    private Double recoveryDuration;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDiagnosisStartTime() {
        return diagnosisStartTime;
    }

    public void setDiagnosisStartTime(String diagnosisStartTime) {
        this.diagnosisStartTime = diagnosisStartTime;
    }

    public String getPrepareStartTime() {
        return prepareStartTime;
    }

    public void setPrepareStartTime(String prepareStartTime) {
        this.prepareStartTime = prepareStartTime;
    }

    public String getResponseStartTime() {
        return responseStartTime;
    }

    public void setResponseStartTime(String responseStartTime) {
        this.responseStartTime = responseStartTime;
    }

    public String getRecoveryStartTime() {
        return recoveryStartTime;
    }

    public void setRecoveryStartTime(String recoveryStartTime) {
        this.recoveryStartTime = recoveryStartTime;
    }

    public String getRecoveryEndTime() {
        return recoveryEndTime;
    }

    public void setRecoveryEndTime(String recoveryEndTime) {
        this.recoveryEndTime = recoveryEndTime;
    }

    public String getStatisticTime() {
        return statisticTime;
    }

    public void setStatisticTime(String statisticTime) {
        this.statisticTime = statisticTime;
    }

    public Double getMaxWaterLevel() {
        return maxWaterLevel;
    }

    public void setMaxWaterLevel(Double maxWaterLevel) {
        this.maxWaterLevel = maxWaterLevel;
    }

    public String getMaxWaterLevelTime() {
        return maxWaterLevelTime;
    }

    public void setMaxWaterLevelTime(String maxWaterLevelTime) {
        this.maxWaterLevelTime = maxWaterLevelTime;
    }

    public Double getPrepareDuration() {
        return prepareDuration;
    }

    public void setPrepareDuration(Double prepareDuration) {
        this.prepareDuration = prepareDuration;
    }

    public Double getResponseDuration() {
        return responseDuration;
    }

    public void setResponseDuration(Double responseDuration) {
        this.responseDuration = responseDuration;
    }

    public Double getRecoveryDuration() {
        return recoveryDuration;
    }

    public void setRecoveryDuration(Double recoveryDuration) {
        this.recoveryDuration = recoveryDuration;
    }
}
