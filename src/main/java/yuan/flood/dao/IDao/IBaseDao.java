package yuan.flood.dao.IDao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Yuan on 2017/1/5.
 * the base dao for every dao operation to implement
 */
public interface IBaseDao <T extends Serializable,PK extends Serializable> {

    public void setSuperSessionFactory(SessionFactory sessionFactory);
    /**
     * get entity from datebase
     * @param id the id of the current table
     * @return the entity of table
     */
    public T get(PK id);

    /**
     * get entity list using hql
     * @param queryString
     * @return
     */
    public List find(String queryString);

    /**
     * get entity list using hql with parameters pattern
     * @param queryString
     * @param values
     * @return
     */
    public List find(String queryString,Object[] values);

    /**
     * get entity list using hql with parameters and their names
     * @param queryString
     * @param paramNames
     * @param values
     * @return
     */
    public List findByNamedParam(String queryString,String[] paramNames,Object[] values);

    /**
     *  save the record of entity
     * @param entity
     */
    public void save(T entity);

    /**
     * delete the record of entity
     * @param entity
     */
    public void delete(T entity);

    /**
     * save if the table does not exist entity ,or update it
     * @param entity
     */
    public void saveOrUpdate(T entity);

    public void saveOrupdate(T entity);

    public void merge(T entity);
}
