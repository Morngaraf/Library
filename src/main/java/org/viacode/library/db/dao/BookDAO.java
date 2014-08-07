package org.viacode.library.db.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.viacode.library.db.model.Book;


/**
 * VIAcode
 * Created by IVolkov on 8/6/2014.
 */
@Transactional
@Repository
public class BookDAO extends BaseDAOImpl<Book> {
}
