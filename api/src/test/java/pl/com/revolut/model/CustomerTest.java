package pl.com.revolut.model;


import org.junit.Test;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.model.identifier.CustomerId;
import pl.pojo.tester.api.assertion.Method;
import pl.pojo.tester.internal.preconditions.NullParameterException;


import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class CustomerTest {
    @Test
    public void testEqualCustomer() {
         assertPojoMethodsFor(Customer.class).testing(Method.GETTER,Method.EQUALS).areWellImplemented();
    }



    @Test(expected=NullParameterException.class)
    public void testCustomerException()
    {
        Customer customer = new Customer();
        customer.setCustomerId(null);
    }
    @Test(expected=IdException.class)
    public void testCustomerIDException() throws pl.com.revolut.exception.NullParameterException, IdException {
        Customer customer = new Customer();
        customer.setCustomerId(new CustomerId("1"));
    }
}

