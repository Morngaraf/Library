package org.viacode.library.service;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.viacode.library.exception.EntityConflictException;
import org.viacode.library.exception.EntityException;
import org.viacode.library.exception.EntityNotFoundException;
import org.viacode.library.db.dao.ClientDAO;
import org.viacode.library.db.model.Book;
import org.viacode.library.db.model.Client;
import org.viacode.library.exception.InternalServerErrorException;
import org.viacode.library.util.ContextUtil;

import java.util.List;

/**
 * VIAcode
 * Created by IVolkov on 8/8/2014.
 */
@Service
public class ClientService {

    @Autowired
    private ClientDAO clientDAO;
    
    @Autowired
    private BookService bookService;

    public Client getClientById(Long id) throws InternalServerErrorException {
        return clientDAO.getById(id);
    }

    public List<Client> getAll() throws InternalServerErrorException {
        return clientDAO.getAll();
    }

    public void deleteClient(Long id) throws InternalServerErrorException, EntityNotFoundException {
        clientDAO.deleteById(id);
    }

    public void addClient(Client client) throws InternalServerErrorException {
        clientDAO.save(client);
    }

    public Client addClientBook(Long clientId, Long bookId) throws InternalServerErrorException, EntityException {
        Book bookToTake = bookService.getBookById(bookId);
        if (bookToTake == null)
            throw new EntityNotFoundException("Entity " + Book.class.getSimpleName() + " with id = " + bookId + " not found in the database.");
        Client client = getClientById(clientId);
        if (client == null)
            throw new EntityNotFoundException("Entity " + Client.class.getSimpleName() + " with id = " + clientId + " not found in the database.");
        if (bookToTake.getQuantity() <= 0)
            throw new EntityConflictException("Entity " + Book.class.getSimpleName() + " with id = " + bookId + " has ZERO quantity.");
        LogManager.getLogger(ClientService.class).trace("FUUUUUUUUUUUUUUUUUUUUUUUU:\r\nCLIENT\r\n{}\r\nBOOKS\r\n{}\r\nBOOK{}", client, client.getBooks(), bookToTake);
        if (!client.getBooks().add(bookToTake))
            throw new EntityConflictException("Entity " + Client.class.getSimpleName() + " with client_id = " + clientId +
                    " already has " + Book.class.getSimpleName() + " with book_id = " + bookId);
        clientDAO.update(client);
        return client;
    }

    public Client returnClientBook(Long clientId, Long bookId) throws InternalServerErrorException, EntityNotFoundException {
        Book bookToReturn = bookService.getBookById(bookId);
        if (bookToReturn == null)
            throw new EntityNotFoundException("Entity " + Book.class.getSimpleName() + " with id = " + bookId + " not found in the database.");
        Client client = getClientById(clientId);
        if (client == null)
            throw new EntityNotFoundException("Entity " + Client.class.getSimpleName() + " with id = " + clientId + " not found in the database.");
        if (!client.getBooks().remove(bookToReturn))
            throw new EntityNotFoundException("Entity " + Client.class.getSimpleName() + " with client_id = " + clientId +
                    " doesn't have " + Book.class.getSimpleName() + " with book_id = " + bookId);
        bookService.returnBook(bookToReturn);
        clientDAO.update(client);
        return client;
    }
}
