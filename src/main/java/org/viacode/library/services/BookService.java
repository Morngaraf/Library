package org.viacode.library.services;

import org.viacode.library.db.dao.BookDAO;
import org.viacode.library.db.model.Book;
import org.viacode.library.utils.ContextUtil;

import java.util.List;

/**
 * VIAcode
 * Created by IVolkov on 8/8/2014.
 */
public class BookService {

    public static Book getBookById(Long id) {
        return getDAO().getById(id);
    }

    public static Book getBook(Book book) {
        return getDAO().find(book);
    }

    public static List<Book> getAll() {
        return getDAO().getAll();
    }

    public static Boolean deleteBook(Long id) {
        getDAO().deleteById(id);
        return true;
    }

    public static Boolean addItem(Book book) {
        getDAO().save(book);
        return true;
    }

    public static Boolean takeBook(Book book) {
        if (book.getQuantity() <= 0) return false;
        book.setQuantity(book.getQuantity() - 1);
        getDAO().update(book);
        return true;
    }

    public static Boolean returnBook(Book book) {
        book.setQuantity(book.getQuantity() + 1);
        getDAO().update(book);
        return true;
    }

    private static BookDAO getDAO() {
        return (BookDAO)(ContextUtil.getApplicationContext().getBean("bookDAO"));
    }
}
