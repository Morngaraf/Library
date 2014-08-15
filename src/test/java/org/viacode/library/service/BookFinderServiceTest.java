package org.viacode.library.service;

import org.apache.solr.common.SolrDocumentList;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.viacode.library.exception.EntityNotFoundException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp//WEB-INF/test/dbContext.xml"})
public class BookFinderServiceTest {

    @Autowired
    private BookFinderService bookFinderService;

    //FIXME: If this unit should be tested then test db must be indexed by solr
    @Ignore
    @Test
    public void testFindBookKeyValue() throws Exception {
        SolrDocumentList solrDocuments = bookFinderService.findBookKeyValue("title", "title1");
        assertTrue(solrDocuments.size() == 1);
        assertTrue(solrDocuments.get(0).containsValue("author1") && solrDocuments.get(0).containsValue("title1"));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testFindBookKeyValueNotFound() throws Exception {
        bookFinderService.findBookKeyValue("title", "titl");
    }
}