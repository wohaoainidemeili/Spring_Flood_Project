package yuan.flood.phase;

import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.service.IService.IEventService;
import yuan.flood.service.IService.ISensorService;

public class PreparePhaseService implements IPhaseService {

    IEventService eventService;
    ISensorService sensorService;
    @Override
    public void executeService(String sesID) {
        //得到参数
        SubscibeEventParams subscibeEventParams = eventService.getRegisteredEventParamsByEventSesID(sesID);
        //计算消息内容，发送消息

        // 做水位预测，只需要传入自己的观测属性，

    }
}
