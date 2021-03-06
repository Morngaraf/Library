package org.viacode.library.res;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.viacode.library.exception.*;
import org.viacode.library.db.json.BookJson;
import org.viacode.library.db.model.Book;
import org.viacode.library.exception.InternalServerErrorException;
import org.viacode.library.service.BookService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/books")
@Produces("application/json")
public class BookResource {

    private final static Logger logger = LogManager.getLogger(BookResource.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private MessageSource responseSource;

    @GET
    @Path("/{book_id}")
    public Response getBookById(@PathParam("book_id") Long bookId) {
        logger.info("Received GET request 'get book by id' where id={}", bookId);

        Book book;
        try {
            book = bookService.getBookById(bookId);
        } catch (InternalServerErrorException | EntityNotFoundException ex) {
            logger.error("Exception during processing 'get book by id' operation : ", ex);
            if (ex instanceof EntityNotFoundException)
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(responseSource.getMessage("noBook", null, LocaleContextHolder.getLocale()))
                    .build();
            return Response.serverError()
                    .entity(responseSource.getMessage("internalServerError", null, LocaleContextHolder.getLocale()))
                    .build();
        }

        logger.info("GET request 'get book by id' where id={} SUCCESS.{}Returning book:{}", bookId, System.lineSeparator(), book);

        return Response.ok().entity(book).build();
    }

    @GET
    public Response getAllBooks() {
        logger.info("Received GET request 'get all books'");

        List<Book> books;
        try {
            books = bookService.getAll();
        } catch (InternalServerErrorException ex) {
            logger.error("Exception during processing 'get all books' operation : ", ex);
            return Response.serverError()
                    .entity(responseSource.getMessage("internalServerError", null, LocaleContextHolder.getLocale()))
                    .build();
        }

        logger.info("GET request 'get all books' SUCCESS.{}Books found={}", System.lineSeparator(), books.size());

        return Response.ok().entity(books).build();
    }

    @DELETE
    @Path("/{book_id}")
    public Response deleteBookById(@PathParam("book_id") Long bookId) {
        logger.info("Received DELETE request 'delete book by id' where id={}", bookId);

        try {
            bookService.deleteBook(bookId);
        } catch (InternalServerErrorException | EntityNotFoundException ex) {
            logger.error("Exception during processing 'delete book by id' operation : ", ex);
            if (ex instanceof EntityNotFoundException)
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(responseSource.getMessage("noBook", null, LocaleContextHolder.getLocale()))
                        .build();
            return Response.serverError()
                    .entity(responseSource.getMessage("internalServerError", null, LocaleContextHolder.getLocale()))
                    .build();
        }

        logger.info("DELETE request 'delete book by id' where id={} SUCCESS.", bookId);

        return Response.ok().build();
    }

    @POST
    @Consumes("application/json")
    public Response addNewBook(BookJson bookJson) {
        logger.info("Received POST request 'add new book' where book={}", bookJson);

        //TODO: normal JSON validation AND override theseJsons.toString()
        Book book = Book.fromJSON(bookJson);
        if (book == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong JSON input!").build();

        try {
            bookService.addBook(book);
        } catch (InternalServerErrorException ex) {
            logger.error("Exception during processing 'add new book' operation : ", ex);
            return Response.serverError()
                    .entity(responseSource.getMessage("internalServerError", null, LocaleContextHolder.getLocale()))
                    .build();
        }

        logger.info("POST request 'add new book' where book={} SUCCESS.{}Book added:{}", bookJson, System.lineSeparator(), book);

        return Response.status(Response.Status.CREATED).build();
    }
}

