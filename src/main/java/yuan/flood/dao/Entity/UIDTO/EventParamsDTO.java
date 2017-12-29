package yuan.flood.dao.Entity.UIDTO;

import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.Entity.UIEntity.SubscribeEventParamsDTO;

/**
 * 事件参数DTO
 */
public class EventParamsDTO {
   public boolean flag;
   public SubscribeEventParamsDTO params;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public SubscribeEventParamsDTO getParams() {
        return params;
    }

    public void setParams(SubscribeEventParamsDTO params) {
        this.params = params;
    }
}
