package yuan.flood.dao;

import org.springframework.stereotype.Repository;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.IDao.IEventDao;

/**
 * Created by Yuan on 2017/2/16.
 */
@Repository
public class EventDaoImpl extends BaseStringIDDaoImpl<SubscibeEventParams,String> implements IEventDao {
}
