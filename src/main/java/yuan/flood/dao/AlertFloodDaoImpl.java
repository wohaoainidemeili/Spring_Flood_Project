package yuan.flood.dao;

import org.springframework.stereotype.Repository;
import yuan.flood.dao.Entity.AlertFloodResult;
import yuan.flood.dao.IDao.IAlertFloodDao;

@Repository
public class AlertFloodDaoImpl extends BaseDaoImpl<AlertFloodResult,Long> implements IAlertFloodDao{

}
