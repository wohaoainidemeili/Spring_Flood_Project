package yuan.flood.dao.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 洪涝预警服务结果记录
 */
public class AlertFloodResult implements Serializable {
    private Long id;
    private Date time;
    private String message;
    private SubscibeEventParams subscibeEventParams;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SubscibeEventParams getSubscibeEventParams() {
        return subscibeEventParams;
    }

    public void setSubscibeEventParams(SubscibeEventParams subscibeEventParams) {
        this.subscibeEventParams = subscibeEventParams;
    }
}
