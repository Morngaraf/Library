package org.viacode.library.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.viacode.library.db.dao.BookDAO;
import org.viacode.library.db.model.Book;
import org.viacode.library.exception.EntityNotFoundException;
import org.viacode.library.exception.InternalServerErrorException;

import java.util.List;

/**
 * VIAcode
 * Created by IVolkov on 8/8/2014.
 */
@Service
public class BookFinderService {

    @Autowired
    private SolrServer solrServer;

    public SolrDocumentList findBookKeyValue(String key, String value) throws InternalServerErrorException, EntityNotFoundException {
        SolrQuery solrQuery = new SolrQuery(key + ":" + value);
        QueryResponse queryResponse;
        try {
            queryResponse = solrServer.query(solrQuery);
        } catch (SolrServerException ex) {
            throw new InternalServerErrorException("Failed to execute sorl query = " + solrQuery.getQuery(), ex);
        }
        if (queryResponse.getResults().size() == 0)
            throw new EntityNotFoundException("No entities found executing sorl query = " + solrQuery.getQuery());
        return queryResponse.getResults();
    }
}
