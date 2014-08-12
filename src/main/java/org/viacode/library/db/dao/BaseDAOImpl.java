package org.viacode.library.db.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * VIAcode
 * Created by IVolkov on 8/6/2014.
 */
@Transactional
public class BaseDAOImpl<T> implements BaseDAO<T> {

    Class<T> clazz;

    @Autowired
    SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setClazz(Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    @Override
    public void save(T t) {
        getCurrentSession().save(t);
    }

    @Override
    public void update(T t) {
        getCurrentSession().update(t);
    }

    @Override
    public void delete(T t) {
        getCurrentSession().delete(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getById(Long id) {
        return (T)getCurrentSession().get(clazz, id);
    }

    @Override
    public void deleteById(Long id) {
        getCurrentSession().delete(getById(id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        String hql = "select a from " + clazz.getSimpleName() + " a";
        return getCurrentSession().createQuery(hql).list();
    }
}
