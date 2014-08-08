package org.viacode.library.res;

import org.viacode.library.db.json.BookJson;
import org.viacode.library.db.json.ClientJson;
import org.viacode.library.db.model.Book;
import org.viacode.library.db.model.Client;
import org.viacode.library.services.BookService;
import org.viacode.library.services.ClientService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Path("/clients")
@Produces("application/json")
public class ClientResource {

    @GET
    @Path("/{client_id}")
    public Response getClient(@PathParam("client_id") Long clientId) {
        Client client = ClientService.getClientById(clientId);
        if (client == null)
            return Response.ok().type(MediaType.TEXT_PLAIN_TYPE).entity("No client with id = " + clientId + " found.").build();
        return Response.ok().entity(client).build();
    }

    @GET
    public Response getAllClients() {
        List<Client> clients = ClientService.getAll();
        if (clients.size() <= 0)
            return Response.ok().type(MediaType.TEXT_PLAIN_TYPE).entity("There are no clients in the library.").build();
        return Response.ok().entity(clients).build();
    }

    @DELETE
    @Path("/{client_id}")
    public Response deleteClient(@PathParam("client_id") Long clientId) {
        if (!ClientService.deleteClient(clientId))
            return Response.serverError().entity(clientId).build();
        return Response.ok().type(MediaType.TEXT_PLAIN_TYPE).entity("Client with id = " + clientId + " successfully deleted.").build();
    }

    @POST
    @Consumes("application/json")
    public Response addNewClient(ClientJson clientJson) {
        Client client = Client.fromJSON(clientJson);
        if (client == null)
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN_TYPE).entity("Wrong JSON input!").build();
        if (!ClientService.addClient(client))
            return Response.serverError().entity(client).build();
        return Response.ok().status(Response.Status.CREATED).entity(client).build();
    }

    @GET
    @Path("/{client_id}/books")
    public Response getClientBooks(@PathParam("client_id") Long clientId) {
        Set<Book> clientBooks = ClientService.getClientById(clientId).getBooks();
        if (clientBooks.size() <= 0)
            return Response.ok().type(MediaType.TEXT_PLAIN_TYPE).entity("There are no books taken by client with id = " + clientId + " .").build();
        return Response.ok().entity(clientBooks).build();
    }

    @PUT
    @Path("/{client_id}/books")
    @Consumes("application/json")
    public Response addClientBook(@PathParam("client_id") Long clientId, BookJson bookJson) {
        Book book = Book.fromJSON(bookJson);
        if (book == null)
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN_TYPE).entity("Wrong JSON input!").build();
        Client client = ClientService.getClientById(clientId);
        if (client == null)
            return Response.ok().type(MediaType.TEXT_PLAIN_TYPE).entity("No client with id = " + clientId + " found.").build();
        if (!ClientService.addClientBook(client, book))
            return Response.serverError().entity(client).build();
        return Response.ok().entity(client.getBooks()).build();
    }

    @DELETE
    @Path("/{client_id}/books/{book_id}")
    @Consumes("application/json")
    public Response returnClientBook(@PathParam("client_id") Long clientId, @PathParam("book_id") Long bookId) {
        Client client = ClientService.getClientById(clientId);
        if (client == null)
            return Response.ok().type(MediaType.TEXT_PLAIN_TYPE).entity("No client with id = " + clientId + " found.").build();
        Book book = BookService.getBookById(bookId);
        if (book == null)
            return Response.ok().type(MediaType.TEXT_PLAIN_TYPE).entity("No book with id = " + bookId + " found.").build();
        if (!ClientService.returnClientBook(client, book))
            return Response.serverError().entity(client).build();
        return Response.ok().entity(client.getBooks()).build();
    }
}

