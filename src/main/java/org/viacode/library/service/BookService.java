package org.viacode.library.service;

import org.springframework.stereotype.Service;
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
@Service
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

    public void addBook(Book book) throws InternalServerErrorException {
        bookDAO.save(book);
    }

    public void takeBook(Book book) throws InternalServerErrorException {
        book.setQuantity(book.getQuantity() - 1);
        bookDAO.update(book);
    }

    public void returnBook(Book book) throws InternalServerErrorException {
        book.setQuantity(book.getQuantity() + 1);
        bookDAO.update(book);
    }
}
