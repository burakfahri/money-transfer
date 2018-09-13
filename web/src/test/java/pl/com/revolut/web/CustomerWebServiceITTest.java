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
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.exception.AccountException;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.exception.PhoneNumberException;

import pl.com.revolut.model.Customer;
import pl.com.revolut.model.identifier.CustomerId;

import java.io.IOException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CustomerWebServiceITTest extends WebServiceTest{

    @Before
    public void before() throws NullParameterException, AccountException {
        customerService.removeAllCustomers();
    }

    @Test
    public void testAddFetchAllCustomers() throws URISyntaxException, IOException, PhoneNumberException, IdException, NullParameterException {
        List<Customer> customerList = createMockCustomer(2);
        customerList.forEach(customer -> {
            try {
                HttpResponse httpResponse = executeHttpEntityEnclosingRequestBase(builder.setPath("/customers").build()
                        ,gson.toJson(customer),HttpPost.METHOD_NAME);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                assertTrue(statusCode > 200 && statusCode <300);
            } catch (URISyntaxException  | IOException e) {
                e.printStackTrace();
            }
        });

        HttpResponse response = executeHttpRequestBase(builder.setPath("/customers").build(), HttpGet.METHOD_NAME);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 200);
        String jsonString = EntityUtils.toString(response.getEntity());
        List<Customer> customers = gson.fromJson(jsonString, ArrayList.class);
        assertEquals(customers.size(), customerList.size());

    }

    @Test
    public void testUpdateAndGetByIdCustomers() throws URISyntaxException, IOException, PhoneNumberException, IdException, NullParameterException {
        List<Customer> customerList = createMockCustomer(2);
        CustomerId customerId = new CustomerId(IdGenerator.generateCustomerId());
        customerList.forEach(customer -> {
            try {
                customer.setCustomerId(customerId);
                customerService.addOrUpdateCustomer(customer);
                HttpResponse httpResponse = executeHttpEntityEnclosingRequestBase(builder.setPath("/customers/"+customerId.getId()).build()
                        ,gson.toJson(customer),HttpPut.METHOD_NAME);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                assertTrue(statusCode >= 200 &&  statusCode < 300 );

            } catch (URISyntaxException  | IOException  | NullParameterException e) {
                e.printStackTrace();
            }
        });
        customerList.forEach(customer -> {
            try {
                HttpResponse httpResponse = executeHttpRequestBase(builder.setPath("/customers/" + customer.getCustomerId()
                                .getId()).build(), HttpGet.METHOD_NAME);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                assertTrue(statusCode == 200);
                String jsonString = EntityUtils.toString(httpResponse.getEntity());
                Customer customer1 = gson.fromJson(jsonString, Customer.class);
                Assertions.assertNotEquals(customer1, customer);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }


    @Test
    public void testRemoveCustomers() throws URISyntaxException, IOException, PhoneNumberException, IdException, NullParameterException {
        List<Customer> customerList = createMockCustomer(2);
        CustomerId customerId = new CustomerId(IdGenerator.generateCustomerId());
        customerList.forEach(customer -> {
            try {
                customer.setCustomerId(customerId);
                customerService.addOrUpdateCustomer(customer);
            } catch (NullParameterException e) {
                e.printStackTrace();
            }
        });
        List<Customer> customerOfServices = customerService.getAllCustomers();

        customerOfServices.forEach(customer -> {
            try {
                HttpResponse httpResponse = executeHttpRequestBase(builder.setPath("/customers/"+customerId.getId()).build()
                        , HttpDelete.METHOD_NAME);
                assertTrue(httpResponse.getStatusLine().getStatusCode() == 202);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        assertEquals(customerService.getAllCustomers().size(),0);
    }
}
