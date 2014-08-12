package org.viacode.library.services;

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

    public Client getClientById(Long id) {
        return getDAO().getById(id);
    }

    public List<Client> getAll() {
        return getDAO().getAll();
    }

    public Boolean deleteClient(Long id) {
        getDAO().deleteById(id);
        return true;
    }

    public Boolean addClient(Client client) {
        getDAO().save(client);
        return true;
    }

    public Boolean addClientBook(Long clientId, Long bookId) {
        Book bookToTake = BookService.getBookService().takeBook(bookId);
        if (bookToTake == null) return false;
        Client client = getClientById(clientId);
        client.getBooks().add(bookToTake);
        getDAO().update(client);
        return true;
    }

    public Boolean returnClientBook(Long clientId, Long bookId) {
        Book bookToReturn = BookService.getBookService().returnBook(bookId);
        if (bookToReturn == null) return false;
        Client client = getClientById(clientId);
        client.getBooks().remove(bookToReturn);
        getDAO().update(client);
        return true;
    }

    private ClientDAO getDAO() {
        return (ClientDAO)(ContextUtil.getApplicationContext().getBean("clientDAO"));
    }
}
