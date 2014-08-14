package org.viacode.library.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.viacode.library.util.ContextUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp//WEB-INF/test/dbBeans.xml"})
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/main/webapp//WEB-INF/test/initialTestData.xml")
public class BookServiceTest {

    private BookService bookService;

    @Before
    public void setUp() throws Exception {
        bookService = (BookService) ContextUtil.getApplicationContext().getBean("bookService");
    }

    @Test
    @ExpectedDatabase("file:src/main/webapp//WEB-INF/test/takeBookTestData.xml")
    public void testTakeBook() throws Exception {
        bookService.takeBook(bookService.getBookById((long)1));
    }

    @Test
    @ExpectedDatabase("file:src/main/webapp//WEB-INF/test/returnBookTestData.xml")
    public void testReturnBook() throws Exception {
        bookService.returnBook(bookService.getBookById((long)1));
        bookService.returnBook(bookService.getBookById((long)2));
    }
}