package org.viacode.library.res;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.viacode.library.exception.*;
import org.viacode.library.db.json.ClientJson;
import org.viacode.library.db.model.Book;
import org.viacode.library.db.model.Client;
import org.viacode.library.exception.InternalServerErrorException;
import org.viacode.library.service.ClientService;
import org.viacode.library.util.ContextUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Path("/clients")
@Produces("application/json")
public class ClientResource {

    private final static Logger logger = LogManager.getLogger(ClientResource.class);

    private ClientService getClientService() throws InternalServerErrorException{
        try {
            return (ClientService) ContextUtil.getApplicationContext().getBean("ClientService");
        } catch (BeansException ex) {
            throw new InternalServerErrorException("Error generating bean ClientService", ex);
        }
    }

    @GET
    @Path("/{client_id}")
    public Response getClient(@PathParam("client_id") Long clientId) {
        Client client;
        try {
            client = getClientService().getClientById(clientId);
        } catch (InternalServerErrorException ex) {
            logger.error("Exception during processing 'get client by id' operation : ", ex);
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }
        if (client == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Sorry, there is no client in the library matching your request.").build();
        return Response.ok().entity(client).build();
    }

    @GET
    public Response getAllClients() {
        List<Client> clients;
        try {
            clients = getClientService().getAll();
        } catch (InternalServerErrorException ex) {
            logger.error("Exception during processing 'get all clients' operation : ", ex);
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }
        return Response.ok().entity(clients).build();
    }

    @DELETE
    @Path("/{client_id}")
    public Response deleteClient(@PathParam("client_id") Long clientId) {
        try {
            getClientService().deleteClient(clientId);
        } catch (InternalServerErrorException | EntityNotFoundException ex) {
            logger.error("Exception during processing 'delete client' operation : ", ex);
            if (ex instanceof EntityNotFoundException)
                return Response.status(Response.Status.NOT_FOUND).entity("Sorry, there is no client in the library matching your request.").build();
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
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
            getClientService().addClient(client);
        } catch (InternalServerErrorException ex) {
            logger.error("Exception during processing 'add new client' operation : ", ex);
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }
        return Response.ok().status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{client_id}/books")
    public Response getClientBooks(@PathParam("client_id") Long clientId) {
        Set<Book> clientBooks;
        try {
            clientBooks = getClientService().getClientById(clientId).getBooks();
        } catch (InternalServerErrorException ex) {
            logger.error("Exception during processing 'get client books' operation : ", ex);
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }
        return Response.ok().entity(clientBooks).build();
    }

    @POST
    @Path("/{client_id}/books/{book_id}/take")
    public Response addClientBook(@PathParam("client_id") Long clientId, @PathParam("book_id") Long bookId) {
        Client client;
        try {
            client = getClientService().addClientBook(clientId, bookId);
        } catch (InternalServerErrorException | EntityNotFoundException ex) {
            logger.error("Exception during processing 'assign book to client' operation : ", ex);
            if (ex instanceof EntityNotFoundException)
                return Response.status(Response.Status.NOT_FOUND).entity("Sorry, there is no client/book in the library matching your request.").build();
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }
        return Response.ok().entity(client).build();
    }

    @POST
    @Path("/{client_id}/books/{book_id}/return")
    public Response returnClientBook(@PathParam("client_id") Long clientId, @PathParam("book_id") Long bookId) {
        Client client;
        try {
            client = getClientService().returnClientBook(clientId, bookId);
        } catch (InternalServerErrorException | EntityNotFoundException ex) {
            logger.error("Exception during processing 'return book to library' operation : ", ex);
            if (ex instanceof EntityNotFoundException)
                return Response.status(Response.Status.NOT_FOUND).entity("Sorry, there is no client/book in the library matching your request.").build();
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }
        return Response.ok(client).build();
    }
}

