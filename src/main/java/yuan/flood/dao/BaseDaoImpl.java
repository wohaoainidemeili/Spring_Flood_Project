package yuan.flood.dao;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import yuan.flood.dao.IDao.IBaseDao;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Yuan on 2017/1/5.
 */
@SuppressWarnings("unchecked")
public class BaseDaoImpl<T extends Serializable,PK extends Serializable> extends HibernateDaoSupport implements IBaseDao<T,PK> {

    //get the entityClass by using the object of the entity
    private Class<T> entityClass;
    public BaseDaoImpl(){
        this.entityClass=null;
        //get the type of T by using getGenericSupperclass function
        Type t= getClass().getGenericSuperclass();

        //get the real class of T by using java reflect
        if (t instanceof ParameterizedType){
            Type[] types=((ParameterizedType)t).getActualTypeArguments();
            this.entityClass=(Class<T>)types[0];
        }
    }

    @Autowired
    @Qualifier("mysessionFactory")
    @Override
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public T get(PK id) {
        return (T)getHibernateTemplate().get(entityClass,id);
    }

    @Override
    public List find(String queryString) {
        return getHibernateTemplate().find(queryString);
    }

    @Override
    public List find(String queryString, Object[] values) {
        return getHibernateTemplate().find(queryString,values);
    }

    @Override
    public List findByNamedParam(String queryString, String[] paramNames, Object[] values) {
        return getHibernateTemplate().findByNamedParam(queryString,paramNames,values);
    }


    @Override
    public void save(T entity) {
        getHibernateTemplate().save(entity);
    }

    @Override
    public void delete(T entity) {
        getHibernateTemplate().delete(entity);
        getHibernateTemplate().flush();
    }

    @Override
    public void saveOrUpdate(T entity) {
        getHibernateTemplate().saveOrUpdate(entity);
        getHibernateTemplate().getSessionFactory().getCurrentSession().getTransaction().commit();
        getHibernateTemplate().getSessionFactory().getCurrentSession().beginTransaction();
    }

    @Override
    public void saveOrupdate(T entity) {
//        Transaction transaction = getHibernateTemplate().getSessionFactory().getCurrentSession().getTransaction();
        getHibernateTemplate().saveOrUpdate(entity);
//        if (transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() && transaction.isActive()) {
//            transaction.commit();
//        }
//        transaction.begin();
    }

    @Override
    public void merge(T entity) {
        getHibernateTemplate().merge(entity);

    }

}
