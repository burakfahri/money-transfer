package pl.com.revolut.service;

import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.CustomerId;
import pl.com.revolut.model.Customer;

import java.util.List;

public interface CustomerService {

    /**
     *
     * @return all the customers
     */

    List<Customer> getAllCustomers();

    /**
     * add or update the customer
     */
    void addOrUpdateCustomer(Customer customer) throws NullParameterException;


    /**
     * remove existing customer by customerId
     * return true if it can delete
     */
    Customer removeCustomer(CustomerId customerId) throws NullParameterException;

    /**
     * @param customerId belongs to customer
     * getcustomerById
     * @return customer by {@code customerId}
     */
    Customer getCustomerById(CustomerId customerId) throws NullParameterException;

    Boolean addAccountToCustomer(CustomerId customerId, AccountId accountId) throws  NullParameterException;

    Boolean removeAccountFromCustomer(CustomerId customerId, AccountId accountId) throws  NullParameterException;

    List<AccountId> getCustomerAccounts(CustomerId customerId) throws NullParameterException;

}
