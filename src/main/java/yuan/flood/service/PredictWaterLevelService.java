package yuan.flood.service;

import org.springframework.beans.factory.annotation.Autowired;
import yuan.flood.dao.Entity.PredictWaterLevelResult;
import yuan.flood.dao.IDao.IPredictWaterLevelResultDao;
import yuan.flood.service.IService.IPredictWaterLevelService;

public class PredictWaterLevelService implements IPredictWaterLevelService{
    @Autowired
    IPredictWaterLevelResultDao predictWaterLevelResultDao;
    @Override
    public PredictWaterLevelResult getLastPredictWaterLevelResultBySESID(String sesID) {
        String countHql = "select count(*) from PredictWaterLevelResult p";
        String predictFindHql = "from PredictWaterLevelResult p where p.subscibeEventParams='" + sesID + "'";
        predictWaterLevelResultDao.find(predictFindHql);

        return null;
    }
}
