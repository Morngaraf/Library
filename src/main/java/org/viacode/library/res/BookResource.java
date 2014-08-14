package org.viacode.library.res;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.BeansException;
import org.viacode.library.exception.*;
import org.viacode.library.db.json.BookJson;
import org.viacode.library.db.model.Book;
import org.viacode.library.exception.InternalServerErrorException;
import org.viacode.library.service.BookService;
import org.viacode.library.util.ContextUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/books")
@Produces("application/json")
public class BookResource {

    private final Logger logger = LogManager.getLogger(BookResource.class);

    private BookService getBookService() {
        return (BookService) ContextUtil.getApplicationContext().getBean("BookService");
    }

    @GET
    @Path("/{book_id}")
    public Response getBook(@PathParam("book_id") Long bookId) {
        Book book;
        try {
            book = getBookService().getBookById(bookId);
        } catch (InternalServerErrorException ex) {
            logger.error("Exception during processing 'get book by id' operation : ", ex);
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }
        if (book == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Sorry, there is no book in the library matching your request.").build();
        return Response.ok().entity(book).build();
    }

    @GET
    public Response getAllBooks() {
        List<Book> books;
        try {
            books = getBookService().getAll();
        } catch (InternalServerErrorException ex) {
            logger.error("Exception during processing 'get all books' operation : ", ex);
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }
        return Response.ok().entity(books).build();
    }

    @DELETE
    @Path("/{book_id}")
    public Response deleteBook(@PathParam("book_id") Long bookId) {
        try {
            getBookService().deleteBook(bookId);
        } catch (InternalServerErrorException | EntityNotFoundException ex) {
            logger.error("Exception during processing 'delete book' operation : ", ex);
            if (ex instanceof EntityNotFoundException)
                return Response.status(Response.Status.NOT_FOUND).entity("Sorry, there is no book in the library matching your request.").build();
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
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
            getBookService().addItem(book);
        } catch (InternalServerErrorException ex) {
            logger.error("Exception during processing 'add new book' operation : ", ex);
            return Response.serverError().entity("Sorry, internal server error occurred. Please, try again later.").build();
        }
        return Response.status(Response.Status.CREATED).build();
    }
}

