package yuan.flood.dao;

import org.springframework.stereotype.Repository;
import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.IDao.ISensorDao;

/**
 * Created by Yuan on 2017/2/8.
 */
@Repository
public class SensorDaoImpl extends BaseStringIDDaoImpl<Sensor,String> implements ISensorDao {
}
