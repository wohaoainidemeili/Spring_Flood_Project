package yuan.flood.phase;

import yuan.flood.dao.Entity.StatisticFloodResult;
import yuan.flood.dao.Entity.SubscibeEventParams;

import java.util.Date;

public class ResponsePhaseService implements IPhaseService {
    @Override
    public void executeService(String sesID, Date date,Object object) {
        //拿到对应的事件基本信息
        // /计算最大水位值，计算水位时间等
        StatisticFloodResult statisticFloodResult = new StatisticFloodResult();


    }
}
