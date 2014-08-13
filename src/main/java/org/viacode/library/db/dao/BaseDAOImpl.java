package org.viacode.library.db.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.viacode.library.EntityNotFoundException;

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

    protected Session getCurrentSession() throws HibernateException {
        return sessionFactory.getCurrentSession();
    }

    public void setClazz(Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    @Override
    public void save(T t) throws HibernateException {
        getCurrentSession().save(t);
    }

    @Override
    public void update(T t) throws HibernateException {
        getCurrentSession().update(t);
    }

    @Override
    public void delete(T t) throws HibernateException {
        getCurrentSession().delete(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getById(Long id) throws HibernateException {
        return (T)getCurrentSession().get(clazz, id);
    }

    @Override
    public void deleteById(Long id) throws HibernateException, EntityNotFoundException {
        T t = getById(id);
        if (t == null) throw new EntityNotFoundException("Entity " + clazz.getSimpleName() + " with id = " + id + " not found in the database.");
        getCurrentSession().delete(getById(id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll() throws HibernateException {
        String hql = "select a from " + clazz.getSimpleName() + " a";
        return getCurrentSession().createQuery(hql).list();
    }
}
