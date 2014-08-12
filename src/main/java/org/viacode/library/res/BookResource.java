package org.viacode.library.res;

import org.viacode.library.db.json.BookJson;
import org.viacode.library.db.model.Book;
import org.viacode.library.services.BookService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/books")
@Produces("application/json")
public class BookResource {

    private BookService bookService = BookService.getBookService();

    @GET
    @Path("/{book_id}")
    public Response getBook(@PathParam("book_id") Long bookId) {
        Book book = bookService.getBookById(bookId);
        if (book == null)
            return Response.ok().type(MediaType.TEXT_PLAIN_TYPE).entity("No book with id = " + bookId + " found.").build();
        return Response.ok().entity(book).build();
    }

    @GET
    public Response getAllBooks() {
        List<Book> books = bookService.getAll();
        if (books.size() <= 0)
            return Response.ok().type(MediaType.TEXT_PLAIN_TYPE).entity("There are no books in the library.").build();
        return Response.ok().entity(books).build();
    }

    @DELETE
    @Path("/{book_id}")
    public Response deleteBook(@PathParam("book_id") Long bookId) {
        if (!bookService.deleteBook(bookId))
            return Response.serverError().entity(bookId).build();
        return Response.ok().build();
    }

    @POST
    @Consumes("application/json")
    public Response addNewBook(BookJson bookJson) {
        Book book = Book.fromJSON(bookJson);
        if (book == null)
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN_TYPE).entity("Wrong JSON input!").build();
        if (!bookService.addItem(book))
            return Response.serverError().entity(book).build();
        return Response.ok().build();
    }
}

