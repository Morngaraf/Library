package org.viacode.library.res;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.viacode.library.exception.EntityNotFoundException;
import org.viacode.library.exception.InternalServerErrorException;
import org.viacode.library.service.BookFinderService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/books")
@Produces("application/json")
public class BookFinderResource {

    private final static Logger logger = LogManager.getLogger(BookFinderResource.class);

    @Autowired
    private BookFinderService bookFinderService;

    @Autowired
    private MessageSource responseSource;

    @POST
    @Path("/find-by-title")
    public Response findBookByTitle(@QueryParam("title") String title) {
        return findBookByKeyValue("title", title);
    }

    @POST
    @Path("/find")
    public Response findBookByAuthor(@QueryParam("author") String title) {
        return findBookByKeyValue("author", title);
    }

    private Response findBookByKeyValue(String key, String value) {
        logger.info("Received POST request 'find book by {}' where {}={}", key, key, value);
        SolrDocumentList documentList;
        try {
            documentList = bookFinderService.findBookKeyValue(key, value);
        } catch (InternalServerErrorException | EntityNotFoundException ex) {
            logger.error("Exception during processing 'find book by {}' operation: ", key, ex);
            if (ex instanceof EntityNotFoundException)
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(responseSource.getMessage("noBook", null, LocaleContextHolder.getLocale()))
                        .build();
            return Response.serverError()
                    .entity(responseSource.getMessage("internalServerError", null, LocaleContextHolder.getLocale()))
                    .build();
        }
        logger.info("POST request 'find book by {}' where {}={} SUCCESS.{}Returning book:{}", key, key, value, System.lineSeparator(), documentList);
        return Response.ok().entity(documentList).build();
    }
}

