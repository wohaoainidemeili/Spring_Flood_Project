package yuan.flood.dao;

import org.springframework.stereotype.Repository;
import yuan.flood.dao.Entity.User;
import yuan.flood.dao.IDao.IUserDao;

/**
 * Created by Yuan on 2017/1/5.
 */
@Repository(value = "userDao")
public class UserDaoImpl extends BaseDaoImpl<User,Long> implements IUserDao {

}
