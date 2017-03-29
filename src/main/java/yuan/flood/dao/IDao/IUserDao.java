package yuan.flood.dao.IDao;

import org.springframework.stereotype.Repository;
import yuan.flood.dao.Entity.User;

/**
 * Created by Yuan on 2017/1/5.
 */
@Repository
public interface IUserDao extends IBaseDao<User,Long> {

}
