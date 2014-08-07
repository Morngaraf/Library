package org.viacode.library;

import org.viacode.library.db.dao.BaseDAO;
import org.viacode.library.db.dao.BookDAO;
import org.viacode.library.db.dao.ClientDAO;
import org.viacode.library.db.model.Book;
import org.viacode.library.db.model.Client;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/library")
public class Library {

    @GET
    @Path("/assign")
    @Produces("application/json")
    public Response assignBook(@QueryParam("client_id") Long client_id,
                               @QueryParam("book_id") Long book_id) {
        ClientDAO clientDAO = (ClientDAO) (ContextUtil.getApplicationContext().getBean("clientDAO"));
        BookDAO bookDAO = (BookDAO) (ContextUtil.getApplicationContext().getBean("bookDAO"));
        Client client = clientDAO.getById(client_id);
        Book book = bookDAO.getById(book_id);
        if (book.getQuantity() <= 0)
            return Response.serverError().entity(book).build();

        book.setQuantity(book.getQuantity() - 1);
        client.getBooks().add(book);
        clientDAO.update(client);
        bookDAO.update(book);
        return Response.ok().entity(client).build();
    }

    @GET
    @Path("/return")
    @Produces("application/json")
    public Response returnBook(@QueryParam("client_id") Long client_id,
                               @QueryParam("book_id") Long book_id) {
        ClientDAO clientDAO = (ClientDAO) (ContextUtil.getApplicationContext().getBean("clientDAO"));
        BookDAO bookDAO = (BookDAO) (ContextUtil.getApplicationContext().getBean("bookDAO"));
        Client client = clientDAO.getById(client_id);
        Book book = bookDAO.getById(book_id);
        client.getBooks().remove(book);
        clientDAO.update(client);
        return Response.ok().entity(client).build();
    }

    @GET
    @Path("/clients/{id}")
    @Produces("application/json")
    public Response getClient(@PathParam("id") Long id) {
        BaseDAO dao = (BaseDAO)(ContextUtil.getApplicationContext().getBean("clientDAO"));
        Client client = (Client)dao.getById(id);
        return Response.ok().entity(client).build();
    }

    @GET
    @Path("/books/{id}")
    @Produces("application/json")
    public Response getBook(@PathParam("id") Long id) {
        BaseDAO dao = (BaseDAO)(ContextUtil.getApplicationContext().getBean("bookDAO"));
        Book book = (Book)dao.getById(id);
        return Response.ok().entity(book).build();
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/clients")
    @SuppressWarnings("unchecked")
    public Response addNewClient(Client client) {
        BaseDAO dao = (BaseDAO)(ContextUtil.getApplicationContext().getBean("clientDAO"));
        dao.save(client);
        return Response.ok().entity(client).build();
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/books")
    @SuppressWarnings("unchecked")
    public Response addNewBook(Book book) {
        BaseDAO dao = (BaseDAO)(ContextUtil.getApplicationContext().getBean("bookDAO"));
        dao.save(book);
        return Response.ok().entity(book).build();
    }

    /*       Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            //do some work
            Client client = new Client();
            client.setFio(fio);
            session.save(client);

            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            throw e;
        }
        finally {
            session.close();
        }*/
}

