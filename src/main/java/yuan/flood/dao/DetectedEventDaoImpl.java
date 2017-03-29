package yuan.flood.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import yuan.flood.dao.Entity.DetectedEvent;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.IDao.IDetectedEventDao;
import yuan.flood.dao.IDao.IEventDao;

/**
 * Created by Yuan on 2017/2/16.
 */
@Repository
public class DetectedEventDaoImpl extends BaseDaoImpl<DetectedEvent,Long> implements IDetectedEventDao {
}
