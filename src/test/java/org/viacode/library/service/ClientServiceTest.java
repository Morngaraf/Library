package org.viacode.library.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.viacode.library.exception.EntityConflictException;
import org.viacode.library.exception.EntityNotFoundException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test/dbBeans.xml"})
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/main/webapp/WEB-INF/test/initialTestData.xml")
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Test(expected = EntityNotFoundException.class)
    public void testAddClientBookWhenBookIdIsWrong() throws Exception {
        clientService.addClientBook((long)1, (long)5);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testAddClientBookWhenClientIdIsWrong() throws Exception {
        clientService.addClientBook((long)5, (long)1);
    }

    @Test(expected = EntityConflictException.class)
    public void testAddClientBookWhenBookIsNotAvailable() throws Exception {
        clientService.addClientBook((long)1, (long)2);
    }

    @Test(expected = EntityConflictException.class)
    public void testAddClientBookWhenClientAlreadyHasIt() throws Exception {
        clientService.addClientBook((long)3, (long)1);
    }

    @Test
    @ExpectedDatabase("file:src/main/webapp/WEB-INF/test/addClientBookTestData.xml")
    public void testAddClientBook() throws Exception {
        assertNotNull(clientService.addClientBook((long) 2, (long) 1));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testReturnClientBookWhenBookIdIsWrong() throws Exception {
        clientService.returnClientBook((long) 1, (long) 5);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testReturnClientBookWhenClientIdIsWrong() throws Exception {
        clientService.returnClientBook((long) 5, (long) 1);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testReturnClientBookWhenClientDoesntHaveBook() throws Exception {
        clientService.returnClientBook((long)1, (long)1);
    }

    @Test
    @ExpectedDatabase("file:src/main/webapp/WEB-INF/test/returnClientBookTestData.xml")
    public void testReturnClientBook() throws Exception {
        assertNotNull(clientService.returnClientBook((long) 3, (long) 1));
    }
}