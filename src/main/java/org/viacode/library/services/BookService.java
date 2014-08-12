package org.viacode.library.services;

import org.hibernate.HibernateException;
import org.springframework.beans.BeansException;
import org.viacode.library.EntityNotFoundException;
import org.viacode.library.db.dao.BookDAO;
import org.viacode.library.db.model.Book;
import org.viacode.library.utils.ContextUtil;

import java.util.List;

/**
 * VIAcode
 * Created by IVolkov on 8/8/2014.
 */
public class BookService {

    private static BookService bookService;

    public static BookService getBookService() {
        if (bookService == null) bookService = new BookService();
        return bookService;
    }

    public Book getBookById(Long id) throws BeansException, HibernateException {
        return getDAO().getById(id);
    }

    public List<Book> getAll() throws BeansException, HibernateException {
        return getDAO().getAll();
    }

    public void deleteBook(Long id) throws BeansException, HibernateException, EntityNotFoundException {
        getDAO().deleteById(id);
    }

    public void addItem(Book book) throws BeansException, HibernateException {
        getDAO().save(book);
    }

    public Book takeBook(Long bookId) throws BeansException, HibernateException {
        Book book = getBookById(bookId);
        if (book.getQuantity() <= 0) return null;
        book.setQuantity(book.getQuantity() - 1);
        getDAO().update(book);
        return book;
    }

    public Book returnBook(Long bookId) throws BeansException, HibernateException {
        Book book = getBookById(bookId);
        book.setQuantity(book.getQuantity() + 1);
        getDAO().update(book);
        return book;
    }

    private BookDAO getDAO() throws BeansException {
        return (BookDAO)(ContextUtil.getApplicationContext().getBean("bookDAO"));
    }
}
