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

    private static BookService bookService;

    public static BookService getBookService() {
        if (bookService == null) bookService = new BookService();
        return bookService;
    }

    public Book getBookById(Long id) {
        return getDAO().getById(id);
    }

    public List<Book> getAll() {
        return getDAO().getAll();
    }

    public Boolean deleteBook(Long id) {
        getDAO().deleteById(id);
        return true;
    }

    public Boolean addItem(Book book) {
        getDAO().save(book);
        return true;
    }

    public Book takeBook(Long bookId) {
        Book book = getBookById(bookId);
        if (book.getQuantity() <= 0) return null;
        book.setQuantity(book.getQuantity() - 1);
        getDAO().update(book);
        return book;
    }

    public Book returnBook(Long bookId) {
        Book book = getBookById(bookId);
        book.setQuantity(book.getQuantity() + 1);
        getDAO().update(book);
        return book;
    }

    private BookDAO getDAO() {
        return (BookDAO)(ContextUtil.getApplicationContext().getBean("bookDAO"));
    }
}
