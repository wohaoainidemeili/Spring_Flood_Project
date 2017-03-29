package yuan.flood.service.IService;

import yuan.flood.dao.Entity.User;

/**
 * Created by Yuan on 2017/1/8.
 */
public interface IUserService {
    public void saveUser(User user);
    public User getUser(String userName);
    public Boolean isLegealUser(User user);
}
