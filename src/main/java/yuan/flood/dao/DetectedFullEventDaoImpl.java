package yuan.flood.dao;

import org.springframework.stereotype.Repository;
import yuan.flood.dao.Entity.DetectedFullEvent;
import yuan.flood.dao.IDao.IBaseDao;
import yuan.flood.dao.IDao.IDetectedFullEventDao;
@Repository
public class DetectedFullEventDaoImpl extends BaseDaoImpl<DetectedFullEvent,Long> implements IDetectedFullEventDao{
}
