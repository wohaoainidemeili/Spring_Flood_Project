package yuan.flood.dao;

import org.springframework.stereotype.Repository;
import yuan.flood.dao.Entity.ObservedProperty;
import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.IDao.IObsPropertyDao;

/**
 * Created by Yuan on 2017/2/9.
 */
@Repository
public class ObsPropertyDaoImpl extends BaseStringIDDaoImpl<ObservedProperty,String> implements IObsPropertyDao {
}
