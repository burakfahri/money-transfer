package pl.com.revolut.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.exception.PhoneNumberException;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.identifier.CustomerId;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CustomerWebServiceITTest extends WebServiceTest{

    @Test
    public void testAddFetchAllCustomers() throws URISyntaxException, IOException, PhoneNumberException, IdException, NullParameterException {
        List<Customer> customerList = createMockCustomer(2);
        customerList.forEach(customer -> {
            try {
                HttpResponse httpResponse = executeHttpCommand(builder.setPath("/customers").build()
                        ,gson.toJson(customer),HttpPost.METHOD_NAME);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                assertTrue(statusCode > 200 && statusCode <300);
            } catch (URISyntaxException  | IOException e) {
                e.printStackTrace();
            }
        });
        URI uri = builder.setPath("/customers").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 200);
        String jsonString = EntityUtils.toString(response.getEntity());
        List<Account> accountList = gson.fromJson(jsonString, ArrayList.class);
        Assertions.assertEquals(accountList.size(),2);
    }

    //@Test
    //public void testUpdateAndGetByIdCustomers() throws URISyntaxException, IOException, PhoneNumberException, IdException, NullParameterException {
    //    List<Customer> customerList = createMockCustomer(2);
    //    CustomerId customerId = new CustomerId(IdGenerator.generateCustomerId());
    //    customerList.forEach(customer -> {
    //        try {
    //            customer.setCustomerId(customerId);
    //            customerService.addOrUpdateCustomer(customer);
    //            HttpResponse httpResponse = executeHttpCommand(builder.setPath("/customers/"+customerId.getId()).build()
    //                    ,gson.toJson(customer),HttpPut.METHOD_NAME);
    //            int statusCode = httpResponse.getStatusLine().getStatusCode();
    //            assertTrue(statusCode >= 200 &&  statusCode < 300 );
//
    //        } catch (URISyntaxException  | IOException  | NullParameterException e) {
    //            e.printStackTrace();
    //        }
    //    });
    //    customerList.forEach(customer -> {
    //        URI uri = null;
    //        try {
    //            uri = builder.setPath("/customers/"+customer.getCustomerId().getId()).build();
    //        } catch (URISyntaxException e) {
    //            e.printStackTrace();
    //        }
    //        HttpGet request = new HttpGet(uri);
    //        HttpResponse response = null;
    //        try {
    //            response = client.execute(request);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        int statusCode = response.getStatusLine().getStatusCode();
    //        assertTrue(statusCode == 200);
    //        String jsonString = null;
    //        try {
    //            jsonString = EntityUtils.toString(response.getEntity());
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        Customer customer1 = gson.fromJson(jsonString, Customer.class);
    //        Assertions.assertEquals(customer1,customer);
//
    //    });
//
    //}
}
