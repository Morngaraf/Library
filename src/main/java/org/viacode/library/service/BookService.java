package org.viacode.library.service;

import org.viacode.library.exception.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.viacode.library.exception.EntityNotFoundException;
import org.viacode.library.db.dao.BookDAO;
import org.viacode.library.db.model.Book;

import java.util.List;

/**
 * VIAcode
 * Created by IVolkov on 8/8/2014.
 */
public class BookService {

    @Autowired
    private BookDAO bookDAO;

    public Book getBookById(Long id) throws InternalServerErrorException {
        return bookDAO.getById(id);
    }

    public List<Book> getAll() throws InternalServerErrorException {
        return bookDAO.getAll();
    }

    public void deleteBook(Long id) throws InternalServerErrorException, EntityNotFoundException {
        bookDAO.deleteById(id);
    }

    public void addItem(Book book) throws InternalServerErrorException {
        bookDAO.save(book);
    }

    public Book takeBook(Long bookId) throws InternalServerErrorException {
        Book book = getBookById(bookId);
        if (book.getQuantity() <= 0) return null;
        book.setQuantity(book.getQuantity() - 1);
        bookDAO.update(book);
        return book;
    }

    public Book returnBook(Long bookId) throws InternalServerErrorException {
        Book book = getBookById(bookId);
        book.setQuantity(book.getQuantity() + 1);
        bookDAO.update(book);
        return book;
    }
}
