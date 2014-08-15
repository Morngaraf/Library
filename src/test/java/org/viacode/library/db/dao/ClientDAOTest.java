package org.viacode.library.db.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.viacode.library.db.model.Client;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test/dbContext.xml"})
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/main/webapp/WEB-INF/test/initialTestData.xml")
public class ClientDAOTest {

    @Autowired
    private ClientDAO clientDAO;

    @Test
    @ExpectedDatabase("file:src/main/webapp/WEB-INF/test/saveClientTestData.xml")
    public void testSave() throws Exception {
        Client client = new Client("fName4", "lName4");
        clientDAO.save(client);
    }

    @Test
    @ExpectedDatabase("file:src/main/webapp/WEB-INF/test/updateClientTestData.xml")
    public void testUpdate() throws Exception {
        Client client = clientDAO.getById((long)3);
        assertNotNull(client);
        assertNotNull(client.getBooks());
        client.getBooks().clear();
        clientDAO.update(client);
        client = clientDAO.getById((long)3);
        assertTrue(client.getBooks().size() == 0);
    }

    @Test
    @ExpectedDatabase("file:src/main/webapp/WEB-INF/test/deleteClientTestData.xml")
    public void testDelete() throws Exception {
        clientDAO.delete(clientDAO.getById((long)2));
    }

    @Test
    public void testGetById() throws Exception {
        Client client = clientDAO.getById((long)2);
        assertNotNull(client);
        assertTrue(client.getFirstName().equals("fName2") && client.getLastName().equals("lName2"));
    }

    @Test
    @ExpectedDatabase("file:src/main/webapp/WEB-INF/test/deleteClientTestData.xml")
    public void testDeleteById() throws Exception {
        clientDAO.deleteById((long)2);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Client> clients = clientDAO.getAll();
        assertNotNull(clients);
        assertTrue(clients.size() == 3);
    }
}