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

    public static Client getClientById(Long id) {
        return getDAO().getById(id);
    }

    public static List<Client> getAll() {
        return getDAO().getAll();
    }

    public static Boolean deleteClient(Long id) {
        getDAO().deleteById(id);
        return true;
    }

    public static Boolean addClient(Client client) {
        getDAO().save(client);
        return true;
    }

    public static Boolean addClientBook(Client client, Book book) {
        Boolean result = BookService.takeBook(book);
        if (!result) {
            BookService.returnBook(book);
        } else {
            client.getBooks().add(book);
            getDAO().update(client);
        }
        return result;
    }

    public static Boolean returnClientBook(Client client, Book book) {
        Boolean result = BookService.returnBook(book);
        if (!result) {
            BookService.takeBook(book);
        } else {
            client.getBooks().remove(book);
            getDAO().update(client);
        }
        return result;
    }

    private static ClientDAO getDAO() {
        return (ClientDAO)(ContextUtil.getApplicationContext().getBean("clientDAO"));
    }
}
