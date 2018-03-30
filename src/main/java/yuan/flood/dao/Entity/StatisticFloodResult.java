package yuan.flood.dao.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 洪涝统计结果表存储
 */
public class StatisticFloodResult implements Serializable {
    private Long id;
    private Date statisticTime;
    private Double maxWaterLevel;
    private Long prepareDuration;
    private Long responseDuration;
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

    public SubscibeEventParams getSubscibeEventParams() {
        return subscibeEventParams;
    }

    public void setSubscibeEventParams(SubscibeEventParams subscibeEventParams) {
        this.subscibeEventParams = subscibeEventParams;
    }
}
