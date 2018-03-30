package yuan.flood.dao;

import org.springframework.stereotype.Repository;
import yuan.flood.dao.Entity.StatisticFloodResult;
import yuan.flood.dao.IDao.IStatisticFloodDao;
@Repository
public class StatisticFloodDaoImpl extends BaseDaoImpl<StatisticFloodResult,Long> implements IStatisticFloodDao {
}
