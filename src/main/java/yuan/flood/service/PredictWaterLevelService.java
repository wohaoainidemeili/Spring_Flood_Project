package yuan.flood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.PredictArrayResult;
import yuan.flood.dao.Entity.PredictWaterLevelResult;
import yuan.flood.dao.IDao.IPredictWaterLevelResultDao;
import yuan.flood.service.IService.IPredictWaterLevelService;

import java.util.List;
@Service
public class PredictWaterLevelService implements IPredictWaterLevelService{
    @Autowired
    IPredictWaterLevelResultDao predictWaterLevelResultDao;

    @Override
    public void savePredictWaterLevelResult(PredictArrayResult predictWaterLevelResult) {
        predictWaterLevelResultDao.save(predictWaterLevelResult);
    }

    @Override
    public PredictWaterLevelResult getPredictWaterLevelResultByTest(String hql) {
        List<PredictWaterLevelResult> predictWaterLevelResultList = predictWaterLevelResultDao.find(hql);
        return predictWaterLevelResultList.get(0);
    }

    @Override
    public List<PredictArrayResult> getLastPredictWaterLevelResultBySESID(String sesID) {
        String countHql = "select count(*) from PredictArrayResult p";
        String predictFindHql = "from PredictArrayResult p where p.subscibeEventParams.eventID='" + sesID + "'";
        List<Long> countList = predictWaterLevelResultDao.find(countHql);
        if (countList.get(0)==0) return null;
        List<PredictArrayResult> predictArrayResultList = predictWaterLevelResultDao.find(predictFindHql);
        if (predictArrayResultList==null||predictArrayResultList.size()==0) return null;
        return predictArrayResultList;
    }

}
