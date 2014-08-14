package org.viacode.library.db.dao;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.viacode.library.db.model.Book;
import org.viacode.library.exception.InternalServerErrorException;

import java.util.HashMap;
import java.util.Map;


/**
 * VIAcode
 * Created by IVolkov on 8/6/2014.
 */
@Transactional
@Repository
public class BookDAO extends BaseDAOImpl<Book> {

    public Book find(Book book) throws InternalServerErrorException {
        Map<String, String> restrictionMap = new HashMap<String, String>();
        restrictionMap.put("author", book.getAuthor());
        restrictionMap.put("title", book.getTitle());
        //FIXME: check restrictionMap.toString() result
        logger.info("Trying to find object {} with properties: {}", clazz.getSimpleName(), restrictionMap);
        try {
            return (Book)getCurrentSession().createCriteria(clazz).add(Restrictions.allEq(restrictionMap)).uniqueResult();
        } catch (HibernateException ex) {
            throw new InternalServerErrorException("Failed to find object " + clazz.getSimpleName() + " with ", ex);
        }
    }
}
