package org.viacode.library.res;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.viacode.library.EntityNotFoundException;
import org.viacode.library.db.json.ClientJson;
import org.viacode.library.db.model.Book;
import org.viacode.library.db.model.Client;
import org.viacode.library.services.ClientService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Path("/clients")
@Produces("application/json")
public class ClientResource {

    private final Logger logger = LoggerFactory.getLogger(ClientResource.class);
    private ClientService clientService = ClientService.getClientService();

    @GET
    @Path("/{client_id}")
    public Response getClient(@PathParam("client_id") Long clientId) {
        Client client;
        try {
            client = clientService.getClientById(clientId);
        } catch (BeansException | HibernateException ex) {
            logger.error(ex.getMessage());
            return Response.serverError().entity(ex).build();
        }
        if (client == null)
            return Response.noContent().entity("No client with id = " + clientId + " found.").build();
        return Response.ok().entity(client).build();
    }

    @GET
    public Response getAllClients() {
        List<Client> clients;
        try {
            clients = clientService.getAll();
        } catch (BeansException | HibernateException ex) {
            logger.error(ex.getMessage());
            return Response.serverError().entity(ex).build();
        }
        if (clients.size() <= 0)
            return Response.noContent().entity("There are no clients in the library.").build();
        return Response.ok().entity(clients).build();
    }

    @DELETE
    @Path("/{client_id}")
    public Response deleteClient(@PathParam("client_id") Long clientId) {
        try {
            clientService.deleteClient(clientId);
        } catch (BeansException | HibernateException | EntityNotFoundException ex) {
            logger.error(ex.getMessage());
            if (ex instanceof EntityNotFoundException)
                return Response.status(Response.Status.NOT_FOUND).entity(ex).build();
            return Response.serverError().entity(ex).build();
        }
        return Response.ok().build();
    }

    @POST
    @Consumes("application/json")
    public Response addNewClient(ClientJson clientJson) {
        //TODO: normal JSON validation.
        Client client = Client.fromJSON(clientJson);
        if (client == null)
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN_TYPE).entity("Wrong JSON input!").build();

        try {
            clientService.addClient(client);
        } catch (BeansException | HibernateException ex) {
            logger.error(ex.getMessage());
            return Response.serverError().entity(ex).build();
        }
        return Response.ok().status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{client_id}/books")
    public Response getClientBooks(@PathParam("client_id") Long clientId) {
        Set<Book> clientBooks;
        try {
            clientBooks = clientService.getClientById(clientId).getBooks();
        } catch (BeansException | HibernateException ex) {
            logger.error(ex.getMessage());
            return Response.serverError().entity(ex).build();
        }
        if (clientBooks.size() <= 0)
            return Response.noContent().entity("There are no books taken by client with id = " + clientId + " .").build();
        return Response.ok().entity(clientBooks).build();
    }

    @POST
    @Path("/{client_id}/books/{book_id}/take")
    public Response addClientBook(@PathParam("client_id") Long clientId, @PathParam("book_id") Long bookId) {
        try {
            clientService.addClientBook(clientId, bookId);
        } catch (BeansException | HibernateException | EntityNotFoundException ex) {
            logger.error(ex.getMessage());
            if (ex instanceof EntityNotFoundException)
                return Response.status(Response.Status.NOT_FOUND).entity(ex).build();
            return Response.serverError().entity(ex).build();
        }
        //FIXME: maybe another status?
        return Response.ok().build();
    }

    @POST
    @Path("/{client_id}/books/{book_id}/return")
    public Response returnClientBook(@PathParam("client_id") Long clientId, @PathParam("book_id") Long bookId) {
        try {
            clientService.returnClientBook(clientId, bookId);
        } catch (BeansException | HibernateException | EntityNotFoundException ex) {
            logger.error(ex.getMessage());
            if (ex instanceof EntityNotFoundException)
                return Response.status(Response.Status.NOT_FOUND).entity(ex).build();
            return Response.serverError().entity(ex).build();
        }
        //FIXME: maybe another status?
        return Response.ok().build();
    }
}

