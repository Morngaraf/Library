package org.viacode.library.db.dao;

import org.springframework.transaction.annotation.Transactional;
import org.viacode.library.exception.EntityNotFoundException;
import org.viacode.library.exception.InternalServerErrorException;

import java.util.List;

/**
 * VIAcode
 * Created by IVolkov on 8/6/2014.
 */
@Transactional
public interface BaseDAO<T> {

    void save(T t) throws InternalServerErrorException;
    void delete(T t) throws InternalServerErrorException;
    void update(T t) throws InternalServerErrorException;
    T getById(Long id) throws InternalServerErrorException, EntityNotFoundException;
    void deleteById(Long id) throws InternalServerErrorException, EntityNotFoundException;
    List<T> getAll() throws InternalServerErrorException;

}
