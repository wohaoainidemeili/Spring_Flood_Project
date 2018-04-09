package yuan.flood.service.IService;

import yuan.flood.dao.Entity.StatisticFloodResult;

import java.util.List;

public interface IStatisticFloodService {
    List<StatisticFloodResult> findStatisticFloodResultByEventId(String eventID);
}
