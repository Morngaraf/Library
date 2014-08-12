package org.viacode.library.res;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.viacode.library.EntityNotFoundException;
import org.viacode.library.db.json.BookJson;
import org.viacode.library.db.model.Book;
import org.viacode.library.services.BookService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/books")
@Produces("application/json")
public class BookResource {

    private final Logger logger = LoggerFactory.getLogger(BookResource.class);
    private BookService bookService = BookService.getBookService();

    @GET
    @Path("/{book_id}")
    public Response getBook(@PathParam("book_id") Long bookId) {
        Book book;
        try {
            book = bookService.getBookById(bookId);
        } catch (BeansException | HibernateException ex) {
            logger.error(ex.getMessage());
            return Response.serverError().entity(ex).build();
        }
        if (book == null)
            return Response.noContent().entity("No book with id = " + bookId + " found.").build();
        return Response.ok().entity(book).build();
    }

    @GET
    public Response getAllBooks() {
        List<Book> books;
        try {
            books = bookService.getAll();
        } catch (BeansException | HibernateException ex) {
            logger.error(ex.getMessage());
            return Response.serverError().entity(ex).build();
        }
        if (books.size() <= 0)
            return Response.noContent().entity("There are no books in the library.").build();
        return Response.ok().entity(books).build();
    }

    @DELETE
    @Path("/{book_id}")
    public Response deleteBook(@PathParam("book_id") Long bookId) {
        try {
            bookService.deleteBook(bookId);
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
    public Response addNewBook(BookJson bookJson) {
        //TODO: normal JSON validation.
        Book book = Book.fromJSON(bookJson);
        if (book == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong JSON input!").build();

        try {
            bookService.addItem(book);
        } catch (BeansException | HibernateException ex) {
            logger.error(ex.getMessage());
            return Response.serverError().entity(ex).build();
        }
        return Response.ok().status(Response.Status.CREATED).build();
    }
}

