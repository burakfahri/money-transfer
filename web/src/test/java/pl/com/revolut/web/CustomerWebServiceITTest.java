package pl.com.revolut.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.exception.*;

import pl.com.revolut.impl.AccountServiceImpl;
import pl.com.revolut.impl.CustomerServiceImpl;
import pl.com.revolut.impl.TransactionServiceImpl;
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.identifier.CustomerId;

import java.io.IOException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {CustomerWebService.class, AccountServiceImpl.class, CustomerServiceImpl.class, TransactionServiceImpl.class})
@EnableAutoConfiguration
public class CustomerWebServiceITTest extends WebServiceTest{

    @Before
    public void before() throws NullParameterException, AccountException, TransactionException {
        transactionService.removeAllTransactions();
        accountService.removeAllAccounts();
        customerService.removeAllCustomers();
    }

    @Test
    public void testAddFetchAllCustomers() throws URISyntaxException, IOException, PhoneNumberException, IdException, NullParameterException {
        List<Customer> customerList = createMockCustomerList(2);
        for (Customer customer : customerList) {
            HttpResponse httpResponse = executeHttpEntityEnclosingRequestBase("/customers"
                    ,gson.toJson(customer),HttpPost.METHOD_NAME);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            assertTrue(statusCode > 200 && statusCode <300);

        }

        HttpResponse response = executeHttpRequestBase("/customers", HttpGet.METHOD_NAME);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 200);
        String jsonString = EntityUtils.toString(response.getEntity());
        List<Customer> customers = gson.fromJson(jsonString, ArrayList.class);
        assertEquals(customers.size(), customerList.size());

    }

    @Test
    public void testUpdateAndGetByIdCustomers() throws URISyntaxException, IOException, PhoneNumberException, IdException, NullParameterException {
        List<Customer> customerList = createMockCustomerList(2);
        CustomerId customerId = new CustomerId(IdGenerator.generateCustomerId());
        for (Customer customer : customerList) {
            //add customer
            customer.setCustomerId(customerId);
            customerService.addOrUpdateCustomer(customer);
            HttpResponse httpResponse = executeHttpEntityEnclosingRequestBase("/customers/"+customerId.getId()
                    ,gson.toJson(customer),HttpPut.METHOD_NAME);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            assertTrue(statusCode >= 200 &&  statusCode < 300 );

            //check customer id
            httpResponse = executeHttpRequestBase("/customers/" + customer.getCustomerId()
                    .getId(), HttpGet.METHOD_NAME);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            assertTrue(statusCode == 200);
            String jsonString = EntityUtils.toString(httpResponse.getEntity());
            Customer customer1 = gson.fromJson(jsonString, Customer.class);
            Assertions.assertNotEquals(customer1, customer);
        }
    }


    @Test
    public void testRemoveCustomers() throws URISyntaxException, IOException, PhoneNumberException, IdException, NullParameterException {
        List<Customer> customerList = createMockCustomerList(2);
        for (Customer customer : customerList) {
            CustomerId customerId = new CustomerId(IdGenerator.generateCustomerId());
            customer.setCustomerId(customerId);
            customerService.addOrUpdateCustomer(customer);
        }
        List<Customer> customerOfServices = customerService.getAllCustomers();

        for (Customer customer : customerOfServices) {
            HttpResponse httpResponse = executeHttpRequestBase("/customers/"+customer.getCustomerId().getId()
                    , HttpDelete.METHOD_NAME);
            assertTrue(httpResponse.getStatusLine().getStatusCode() == 200);
        }
    }
}
