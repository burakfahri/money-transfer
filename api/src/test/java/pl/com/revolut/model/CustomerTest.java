package pl.com.revolut.model;


import org.junit.Test;
import pl.pojo.tester.api.assertion.Method;


import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class CustomerTest {
    @Test
    public void testCustomer() {
         assertPojoMethodsFor(Customer.class).testing(Method.values()).areWellImplemented();
    }

    @Test(expected=NullPointerException.class)
    public void testCustomerException()
    {
        Customer customer = new Customer();
        customer.setCustomerId(null);
    }
}
