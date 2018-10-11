package pl.com.revolut.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.exception.AccountException;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.exception.PhoneNumberException;
import pl.com.revolut.impl.CustomerServiceImpl;
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.PhoneNumber;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.CustomerId;
import pl.com.revolut.service.CustomerService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {CustomerServiceImpl.class,AccountServiceImpl.class,TransactionServiceImpl.class})
public class CustomerServiceImplTest extends  AbstractServiceImplTest{

    @Autowired private CustomerService customerService;

    @Before
    public void setup() throws PhoneNumberException, NullParameterException, IdException {

        customerList.clear();
    }

    @Test
    public void addOrUpdateCustomerTest() throws PhoneNumberException, IdException, NullParameterException, AccountException {

        //ADD CUSTOMER
        List<Customer> customers = createMockCustomer(1);
        customerService.addOrUpdateCustomer(customers.get(0));
        Customer customer = customerService.getCustomerById(customers.get(0).getCustomerId());
        Assertions.assertTrue(customerList.contains(customer));


        //UPDATE CUSTOMER
        customer.setCustomerSurname("FF");

        customerService.addOrUpdateCustomer(customer);
        Customer customer1 = customerService.getCustomerById(customers.get(0).getCustomerId());

        Assertions.assertEquals("FF",customer1.getCustomerSurname());
        customerService.removeCustomer(customers.get(0).getCustomerId());

    }


    @Test
    public void removeCustomer() throws PhoneNumberException, IdException, NullParameterException, AccountException {

        List<Customer> customers = createMockCustomer(1);
        customerService.addOrUpdateCustomer(customers.get(0));
        Assertions.assertEquals(customerService.getCustomerById(customers.get(0).getCustomerId()), customers.get(0));
        customerService.removeCustomer(customers.get(0).getCustomerId());
        List<Customer> customerList1 = customerService.getAllCustomers();
        Assertions.assertNotEquals(customerList1,customers);
    }

    @Test(expected = AccountException.class)
    public void removeCustomerWithExceptionTest() throws PhoneNumberException, IdException, NullParameterException, AccountException {


        List<Customer> customers = createMockCustomer(1);
        customerService.addOrUpdateCustomer(customers.get(0));
        customerService.addAccountToCustomer(customers.get(0).getCustomerId(),new AccountId("ACC-1"));
        customerService.removeCustomer(customers.get(0).getCustomerId());
    }

    @Test
    public void getCustomerByIdTest() throws PhoneNumberException, IdException, NullParameterException, AccountException {


        List<Customer> customers = createMockCustomer(1);
        customerService.addOrUpdateCustomer(customers.get(0));
        Assertions.assertEquals(customerService.getCustomerById(customers.get(0).getCustomerId()),customers.get(0));
    }


    @Test
    public void addCustomerToAccountTest() throws PhoneNumberException, IdException, NullParameterException, AccountException {


        List<Customer> customers = createMockCustomer(1);
        AccountId accountId = new AccountId("ACC-1");
        customerService.addAccountToCustomer(customers.get(0).getCustomerId(),accountId);
        List<AccountId> accountIds = customerService.getCustomerAccounts(customers.get(0).getCustomerId());
        Assertions.assertEquals(accountIds.get(0),accountId);
    }


    @Test
    public void addCustomerToAccountTwiceTest() throws PhoneNumberException, IdException, NullParameterException, AccountException {


        List<Customer> customers = createMockCustomer(1);
        customerService.addOrUpdateCustomer(customers.get(0));
        AccountId accountId = new AccountId("ACC-1");
        customerService.addAccountToCustomer(customers.get(0).getCustomerId(),accountId);
        customerService.addAccountToCustomer(customers.get(0).getCustomerId(),accountId);
        List<AccountId> accountIds = customerService.getCustomerAccounts(customers.get(0).getCustomerId());
        Assertions.assertEquals(accountIds.get(0),accountId);
    }

    @Test
    public void removeAccountFromCustomerTest() throws PhoneNumberException, IdException, NullParameterException, AccountException {
        List<Customer> customers = createMockCustomer(1);
        AccountId accountId = new AccountId("ACC-1");
        customerService.addAccountToCustomer(customers.get(0).getCustomerId(),accountId);
        Assertions.assertEquals(customerService.getCustomerAccounts(customers.get(0).getCustomerId()).size(),1);
        customerService.removeAccountFromCustomer(customers.get(0).getCustomerId(),accountId);
        Assertions.assertEquals(customerService.getCustomerAccounts(customers.get(0).getCustomerId()).size(),0);
    }

    @Test
    public void removeAccountFromCustomerTwiceTest() throws PhoneNumberException, IdException, NullParameterException, AccountException {
        List<Customer> customers = createMockCustomer(1);
        AccountId accountId = new AccountId("ACC-1");
        customerService.addAccountToCustomer(customers.get(0).getCustomerId(),accountId);
        Assertions.assertEquals(customerService.getCustomerAccounts(customers.get(0).getCustomerId()).size(),1);
        customerService.removeAccountFromCustomer(customers.get(0).getCustomerId(),accountId);
        Assertions.assertEquals(customerService.getCustomerAccounts(customers.get(0).getCustomerId()).size(),0);
        customerService.removeAccountFromCustomer(customers.get(0).getCustomerId(),accountId);
        Assertions.assertEquals(customerService.getCustomerAccounts(customers.get(0).getCustomerId()).size(),0);

    }


}
