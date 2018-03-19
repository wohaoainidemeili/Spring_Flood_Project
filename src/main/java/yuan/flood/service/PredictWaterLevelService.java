package yuan.flood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.PredictWaterLevelResult;
import yuan.flood.dao.IDao.IPredictWaterLevelResultDao;
import yuan.flood.service.IService.IPredictWaterLevelService;

import java.util.List;
@Service
public class PredictWaterLevelService implements IPredictWaterLevelService{
    @Autowired
    IPredictWaterLevelResultDao predictWaterLevelResultDao;

    @Override
    public void savePredictWaterLevelResult(PredictWaterLevelResult predictWaterLevelResult) {
        predictWaterLevelResultDao.save(predictWaterLevelResult);
    }

    @Override
    public PredictWaterLevelResult getPredictWaterLevelResultByTest(String hql) {
        List<PredictWaterLevelResult> predictWaterLevelResultList = predictWaterLevelResultDao.find(hql);
        return predictWaterLevelResultList.get(0);
    }

    @Override
    public PredictWaterLevelResult getLastPredictWaterLevelResultBySESID(String sesID) {
        String countHql = "select count(*) from PredictWaterLevelResult p";
        String predictFindHql = "from PredictWaterLevelResult p where p.subscibeEventParams='" + sesID + "'";
        predictWaterLevelResultDao.find(predictFindHql);

        return null;
    }
}
