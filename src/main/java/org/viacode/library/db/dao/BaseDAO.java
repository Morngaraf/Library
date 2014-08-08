package org.viacode.library.db.dao;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * VIAcode
 * Created by IVolkov on 8/6/2014.
 */
@Transactional
public interface BaseDAO<T> {

    void save(T t);
    void delete(T t);
    void update(T t);
    T getById(Long id);
    void deleteById(Long id);
    List<T> getAll();

}
