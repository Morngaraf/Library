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
            return (ClientService) ContextUtil.getApplicationContext().getBean("clientService");
        } catch (BeansException ex) {
            throw new InternalServerErrorException("Error generating bean ClientService", ex);
        }
    }

    @GET
    @Path("/{client_id}")
    public Response getClientById(@PathParam("client_id") Long clientId) {
        logger.info("Received GET request 'get client by id' where id={}", clientId);

        Client client;
        try {
            client = getClientService().getClientById(clientId);
        } catch (InternalServerErrorException ex) {
            logger.error("Exception during processing 'get client by id' operation : ", ex);
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }

        logger.info("GET request 'get client by id' where id={} SUCCESS.{}Returning client:{}", clientId, System.lineSeparator(), client);

        if (client == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Sorry, there is no client in the library matching your request.").build();
        return Response.ok().entity(client).build();
    }

    @GET
    public Response getAllClients() {
        logger.info("Received GET request 'get all clients'");

        List<Client> clients;
        try {
            clients = getClientService().getAll();
        } catch (InternalServerErrorException ex) {
            logger.error("Exception during processing 'get all clients' operation : ", ex);
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }

        logger.info("GET request 'get all clients' SUCCESS.{}Clients found={}", System.lineSeparator(), clients.size());

        return Response.ok().entity(clients).build();
    }

    @DELETE
    @Path("/{client_id}")
    public Response deleteClientById(@PathParam("client_id") Long clientId) {
        logger.info("Received DELETE request 'delete client by id' where id={}", clientId);

        try {
            getClientService().deleteClient(clientId);
        } catch (InternalServerErrorException | EntityNotFoundException ex) {
            logger.error("Exception during processing 'delete client' operation : ", ex);
            if (ex instanceof EntityNotFoundException)
                return Response.status(Response.Status.NOT_FOUND).entity("Sorry, there is no client in the library matching your request.").build();
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }

        logger.info("DELETE request 'delete client by id' where id={} SUCCESS.", clientId);

        return Response.ok().build();
    }

    @POST
    @Consumes("application/json")
    public Response addNewClient(ClientJson clientJson) {
        logger.info("Received POST request 'add new client' where client={}", clientJson);

        //TODO: normal JSON validation AND override theseJsons.toString()
        Client client = Client.fromJSON(clientJson);
        if (client == null)
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN_TYPE).entity("Wrong JSON input!").build();

        try {
            getClientService().addClient(client);
        } catch (InternalServerErrorException ex) {
            logger.error("Exception during processing 'add new client' operation : ", ex);
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }

        logger.info("POST request 'add new client' where client={} SUCCESS.{}Client added:{}", clientJson, System.lineSeparator(), client);

        return Response.ok().status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{client_id}/books")
    public Response getClientBooks(@PathParam("client_id") Long clientId) {
        logger.info("Received GET request 'get client books by client_id' where client_id={}", clientId);

        Set<Book> clientBooks;
        try {
            clientBooks = getClientService().getClientById(clientId).getBooks();
        } catch (InternalServerErrorException ex) {
            logger.error("Exception during processing 'get client books' operation : ", ex);
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }

        logger.info("GET request 'get client books by client_id' where client_id={} SUCCESS.{}Books found for this client={}",
                clientId, System.lineSeparator(), clientBooks.size());

        return Response.ok().entity(clientBooks).build();
    }

    @POST
    @Path("/{client_id}/books/{book_id}/take")
    public Response addClientBook(@PathParam("client_id") Long clientId, @PathParam("book_id") Long bookId) {
        logger.info("Received POST request 'add client book' where client_id={} & book_id={}", clientId, bookId);

        Client client;
        try {
            client = getClientService().addClientBook(clientId, bookId);
        } catch (InternalServerErrorException | EntityException ex) {
            logger.error("Exception during processing 'assign book to client' operation : ", ex);
            if (ex instanceof EntityNotFoundException)
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Sorry, there is no client/book in the library matching your request.").build();
            if (ex instanceof EntityConflictException)
                return Response.status(Response.Status.CONFLICT)
                        .entity("Sorry, you already have this book or it's not available in library right now.").build();
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }

        logger.info("POST request 'add client book' where client_id={} & book_id={} SUCCESS.{}Returning client:{}",
                clientId, bookId, System.lineSeparator(), client);

        return Response.ok().entity(client).build();
    }

    @POST
    @Path("/{client_id}/books/{book_id}/return")
    public Response returnClientBook(@PathParam("client_id") Long clientId, @PathParam("book_id") Long bookId) {
        logger.info("Received POST request 'return client book' where client_id={} & book_id={}", clientId, bookId);

        Client client;
        try {
            client = getClientService().returnClientBook(clientId, bookId);
        } catch (InternalServerErrorException | EntityNotFoundException ex) {
            logger.error("Exception during processing 'return book to library' operation : ", ex);
            if (ex instanceof EntityNotFoundException)
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Sorry, there is no client/book in the library matching your request.").build();
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }

        logger.info("POST request 'return client book' where client_id={} & book_id={} SUCCESS.{}Returning client:{}",
                clientId, bookId, System.lineSeparator(), client);

        return Response.ok(client).build();
    }
}

