package org.viacode.library.res;

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

    private ClientService clientService = ClientService.getClientService();

    @GET
    @Path("/{client_id}")
    public Response getClient(@PathParam("client_id") Long clientId) {
        Client client = clientService.getClientById(clientId);
        if (client == null)
            return Response.ok().type(MediaType.TEXT_PLAIN_TYPE).entity("No client with id = " + clientId + " found.").build();
        return Response.ok().entity(client).build();
    }

    @GET
    public Response getAllClients() {
        List<Client> clients = clientService.getAll();
        if (clients.size() <= 0)
            return Response.ok().type(MediaType.TEXT_PLAIN_TYPE).entity("There are no clients in the library.").build();
        return Response.ok().entity(clients).build();
    }

    @DELETE
    @Path("/{client_id}")
    public Response deleteClient(@PathParam("client_id") Long clientId) {
        if (!clientService.deleteClient(clientId))
            return Response.serverError().entity(clientId).build();
        return Response.ok().type(MediaType.TEXT_PLAIN_TYPE).entity("Client with id = " + clientId + " successfully deleted.").build();
    }

    @POST
    @Consumes("application/json")
    public Response addNewClient(ClientJson clientJson) {
        Client client = Client.fromJSON(clientJson);
        if (client == null)
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN_TYPE).entity("Wrong JSON input!").build();
        if (!clientService.addClient(client))
            return Response.serverError().entity(client).build();
        return Response.ok().status(Response.Status.CREATED).entity(client).build();
    }

    @GET
    @Path("/{client_id}/books")
    public Response getClientBooks(@PathParam("client_id") Long clientId) {
        Set<Book> clientBooks = clientService.getClientById(clientId).getBooks();
        if (clientBooks.size() <= 0)
            return Response.ok().type(MediaType.TEXT_PLAIN_TYPE).entity("There are no books taken by client with id = " + clientId + " .").build();
        return Response.ok().entity(clientBooks).build();
    }

    @POST
    @Path("/{client_id}/books/{book_id}/take")
    public Response addClientBook(@PathParam("client_id") Long clientId, @PathParam("book_id") Long bookId) {
        if (!clientService.addClientBook(clientId, bookId))
            return Response.serverError().build();
        return Response.ok().build();
    }

    @POST
    @Path("/{client_id}/books/{book_id}/return")
    public Response returnClientBook(@PathParam("client_id") Long clientId, @PathParam("book_id") Long bookId) {
        if (!clientService.returnClientBook(clientId, bookId))
            return Response.serverError().build();
        return Response.ok().build();
    }
}

