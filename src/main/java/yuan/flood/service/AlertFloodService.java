package yuan.flood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.AlertFloodResult;
import yuan.flood.dao.Entity.PredictArrayResult;
import yuan.flood.dao.IDao.IAlertFloodDao;
import yuan.flood.service.IService.IAlertFloodService;

import java.util.List;

@Service
public class AlertFloodService implements IAlertFloodService{
    @Autowired
    private IAlertFloodDao alertFloodDao;
    @Override
    public List<AlertFloodResult> findAlertFloodResultByEventID(String eventID) {
        String countHql = "select count(*) from AlertFloodResult p";
        String predictFindHql = "from AlertFloodResult p where p.subscibeEventParams.eventID='" + eventID + "'";
        List<Long> countList = alertFloodDao.find(countHql);
        if (countList.get(0)==0) return null;
        List<AlertFloodResult> alertFloodResultList = alertFloodDao.find(predictFindHql);
        if (alertFloodResultList==null||alertFloodResultList.size()==0) return null;
        return alertFloodResultList;
    }
}
