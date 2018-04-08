package yuan.flood.dao;

import org.springframework.stereotype.Repository;
import yuan.flood.dao.Entity.PredictArrayResult;
import yuan.flood.dao.Entity.PredictWaterLevelResult;
import yuan.flood.dao.IDao.IPredictWaterLevelResultDao;

@Repository
public class PredictWaterLevelResultDaoImpl extends BaseDaoImpl<PredictArrayResult,Long> implements IPredictWaterLevelResultDao {
}
