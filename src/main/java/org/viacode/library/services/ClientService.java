package org.viacode.library.services;

import org.hibernate.HibernateException;
import org.springframework.beans.BeansException;
import org.viacode.library.EntityNotFoundException;
import org.viacode.library.db.dao.ClientDAO;
import org.viacode.library.db.model.Book;
import org.viacode.library.db.model.Client;
import org.viacode.library.utils.ContextUtil;

import java.util.List;

/**
 * VIAcode
 * Created by IVolkov on 8/8/2014.
 */
public class ClientService {

    private static ClientService clientService;

    public static ClientService getClientService() {
        if (clientService == null) clientService = new ClientService();
        return clientService;
    }

    public Client getClientById(Long id) throws BeansException, HibernateException {
        return getDAO().getById(id);
    }

    public List<Client> getAll() throws BeansException, HibernateException {
        return getDAO().getAll();
    }

    public void deleteClient(Long id) throws BeansException, HibernateException, EntityNotFoundException {
        getDAO().deleteById(id);
    }

    public void addClient(Client client) throws BeansException, HibernateException {
        getDAO().save(client);
    }

    public void addClientBook(Long clientId, Long bookId) throws BeansException, HibernateException, EntityNotFoundException {
        Book bookToTake = BookService.getBookService().takeBook(bookId);
        if (bookToTake == null)
            throw new EntityNotFoundException("Entity " + Book.class.getSimpleName() + " with id = " + bookId + " not found in the database.");
        Client client = getClientById(clientId);
        if (client == null)
            throw new EntityNotFoundException("Entity " + Client.class.getSimpleName() + " with id = " + clientId + " not found in the database.");
        client.getBooks().add(bookToTake);
        getDAO().update(client);
    }

    public void returnClientBook(Long clientId, Long bookId) throws BeansException, HibernateException, EntityNotFoundException {
        Book bookToReturn = BookService.getBookService().returnBook(bookId);
        if (bookToReturn == null)
            throw new EntityNotFoundException("Entity " + Book.class.getSimpleName() + " with id = " + bookId + " not found in the database.");
        Client client = getClientById(clientId);
        if (client == null)
            throw new EntityNotFoundException("Entity " + Client.class.getSimpleName() + " with id = " + clientId + " not found in the database.");
        client.getBooks().remove(bookToReturn);
        getDAO().update(client);
    }

    private ClientDAO getDAO() throws BeansException {
        return (ClientDAO)(ContextUtil.getApplicationContext().getBean("clientDAO"));
    }
}
