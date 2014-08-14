package org.viacode.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public Client addClientBook(Long clientId, Long bookId) throws InternalServerErrorException, EntityNotFoundException {
        Book bookToTake = getBookService().takeBook(bookId);
        if (bookToTake == null)
            throw new EntityNotFoundException("Entity " + Book.class.getSimpleName() + " with id = " + bookId + " not found in the database.");
        Client client = getClientById(clientId);
        if (client == null)
            throw new EntityNotFoundException("Entity " + Client.class.getSimpleName() + " with id = " + clientId + " not found in the database.");
        client.getBooks().add(bookToTake);
        clientDAO.update(client);
        return client;
    }

    public Client returnClientBook(Long clientId, Long bookId) throws InternalServerErrorException, EntityNotFoundException {
        Book bookToReturn = getBookService().returnBook(bookId);
        if (bookToReturn == null)
            throw new EntityNotFoundException("Entity " + Book.class.getSimpleName() + " with id = " + bookId + " not found in the database.");
        Client client = getClientById(clientId);
        if (client == null)
            throw new EntityNotFoundException("Entity " + Client.class.getSimpleName() + " with id = " + clientId + " not found in the database.");
        client.getBooks().remove(bookToReturn);
        clientDAO.update(client);
        return client;
    }

    private BookService getBookService() {
        return (BookService) ContextUtil.getApplicationContext().getBean("BookService");
    }
}
