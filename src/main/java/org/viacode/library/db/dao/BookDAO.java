package org.viacode.library.db.dao;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.viacode.library.db.model.Book;

import java.util.HashMap;
import java.util.Map;


/**
 * VIAcode
 * Created by IVolkov on 8/6/2014.
 */
@Transactional
@Repository
public class BookDAO extends BaseDAOImpl<Book> {

    public Book find(Book book) throws HibernateException {
        Map<String, String> restrictionMap = new HashMap<String, String>();
        restrictionMap.put("author", book.getAuthor());
        restrictionMap.put("title", book.getTitle());
        return (Book)getCurrentSession().createCriteria(clazz).add(Restrictions.allEq(restrictionMap)).uniqueResult();
    }
}
