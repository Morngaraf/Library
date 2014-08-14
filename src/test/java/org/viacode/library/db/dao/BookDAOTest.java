package org.viacode.library.db.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.viacode.library.db.model.Book;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test/dbBeans.xml"})
@Transactional
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/main/webapp/WEB-INF/test/initialTestData.xml")
public class BookDAOTest {

    @Autowired
    private BookDAO bookDAO;

    @Test
    public void testFind() throws Exception {
        Book book = new Book("title1", "author1");
        assertNotNull(bookDAO.find(book));
    }
}