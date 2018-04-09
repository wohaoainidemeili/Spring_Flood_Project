package yuan.flood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.StatisticFloodResult;
import yuan.flood.dao.IDao.IStatisticFloodDao;
import yuan.flood.service.IService.IStatisticFloodService;

import java.util.List;

@Service
public class StatisticFloodService implements IStatisticFloodService {
    @Autowired
    IStatisticFloodDao statisticFloodDao;
    @Override
    public List<StatisticFloodResult> findStatisticFloodResultByEventId(String eventID) {
        String countHql = "select count(*) from StatisticFloodResult p";
        String predictFindHql = "from StatisticFloodResult p where p.subscibeEventParams.eventID='" + eventID + "'";
        List<Long> countList = statisticFloodDao.find(countHql);
        if (countList.get(0)==0) return null;
        List<StatisticFloodResult> statisticFloodResultList = statisticFloodDao.find(predictFindHql);
        if (statisticFloodResultList==null||statisticFloodResultList.size()==0) return null;
        return statisticFloodResultList;
    }

}
