package pl.com.revolut.model;


import org.junit.Assert;
import org.junit.Test;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.PhoneNumberException;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.CustomerId;
import pl.pojo.tester.api.assertion.Method;
import pl.pojo.tester.internal.preconditions.NullParameterException;


import java.math.BigDecimal;
import java.util.Date;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class CustomerTest {
    @Test
    public void testEqualCustomer() {
         assertPojoMethodsFor(Customer.class).testing(Method.GETTER,Method.TO_STRING).areWellImplemented();
    }


    @Test
    public void testAccountConstructorAndSetters() throws pl.com.revolut.exception.NullParameterException, IdException {
        Date date = new Date();
        PhoneNumber phoneNumber = null;
        try {
            phoneNumber = new PhoneNumber(210,210,210);
        } catch (PhoneNumberException e) {
            e.printStackTrace();
        }
        Customer customer =  new Customer(new CustomerId("CUS-1"),"BURAK","CABUK",phoneNumber, date);
        Customer customer1 = new Customer(new CustomerId("CUS-1"),"BURAK","CABUK",phoneNumber, date);
        Customer customer2 = new Customer();
        Customer customer3 = new Customer();
        customer3.setCustomerId(new CustomerId("CUS-1"));
        customer3.setCustomerName("BURAK");
        customer3.setCustomerSurname("CABUK");
        customer3.setCustomerPhone(phoneNumber);
        customer3.setAttendDate(date);
        Assert.assertTrue(customer.equals(customer1));
        Assert.assertEquals(customer,customer3);
        Assert.assertNotEquals(customer,customer2);
    }


    @Test(expected=IdException.class)
    public void testCustomerIDException() throws pl.com.revolut.exception.NullParameterException, IdException {
        Customer customer = new Customer();
        customer.setCustomerId(new CustomerId("1"));
    }
}

