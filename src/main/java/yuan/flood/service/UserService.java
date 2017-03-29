package yuan.flood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.User;
import yuan.flood.dao.IDao.IUserDao;
import yuan.flood.service.IService.IUserService;

import java.util.List;

/**
 * Created by Yuan on 2017/1/5.
 */
@Service(value = "userService")
public class UserService implements IUserService {
    @Autowired
    private IUserDao userDao;
    public void saveUser(User user){
        userDao.save(user);
    }

    @Override
    public User getUser(String userName) {
        User user=null;
        List<User> users=userDao.find("from User u where u.userID=?",new String[]{userName});
        if (users.size()>0){
            user=users.get(0);
        }
        return user;
    }

    @Override
    public Boolean isLegealUser(User user) {
        User dbuser=this.getUser(user.getUserID());
        if (dbuser!=null) return true;
        else return false;
    }


    public IUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

}
