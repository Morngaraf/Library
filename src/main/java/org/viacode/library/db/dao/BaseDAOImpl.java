package org.viacode.library.db.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.viacode.library.exception.EntityNotFoundException;
import org.viacode.library.exception.InternalServerErrorException;

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

    protected final Logger logger = LogManager.getLogger(clazz);

    protected Session getCurrentSession() throws InternalServerErrorException {
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException ex) {
            throw new InternalServerErrorException("Can not get access to the current session", ex);
        }
    }

    public void setClazz(Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    @Override
    public void save(T t) throws InternalServerErrorException {
        try {
            getCurrentSession().save(t);
        } catch (HibernateException ex) {
            throw new InternalServerErrorException("Saving new object failed: " + t, ex);
        }
    }

    @Override
    public void update(T t) throws InternalServerErrorException {
        try {
            getCurrentSession().update(t);
        } catch (HibernateException ex) {
            throw new InternalServerErrorException("Updating object failed: " + t, ex);
        }
    }

    @Override
    public void delete(T t) throws InternalServerErrorException {
        try {
            getCurrentSession().delete(t);
        } catch (HibernateException ex) {
            throw new InternalServerErrorException("Failed to delete object: " + t, ex);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getById(Long id) throws InternalServerErrorException {
        try {
            return (T)getCurrentSession().get(clazz, id);
        } catch (HibernateException ex) {
            throw new InternalServerErrorException("Failed to find object " + clazz.getSimpleName() + " with id= " + id, ex);
        }
    }

    @Override
    public void deleteById(Long id) throws InternalServerErrorException, EntityNotFoundException {
        try {
            T t = getById(id);
            if (t == null) throw new EntityNotFoundException("Entity " + clazz.getSimpleName() + " with id = " + id + " not found in the database");
            getCurrentSession().delete(t);
        } catch (HibernateException ex) {
            throw new InternalServerErrorException("Failed to delete object " + clazz.getSimpleName() + " with id= " + id, ex);
        }


    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll() throws InternalServerErrorException {
        try {
            String hql = "select a from " + clazz.getSimpleName() + " a";
            return getCurrentSession().createQuery(hql).list();
        } catch (HibernateException ex) {
            throw new InternalServerErrorException("Failed to get objects of type " + clazz.getSimpleName(), ex);
        }
    }
}
