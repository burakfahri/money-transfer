package pl.com.revolut.service;

import pl.com.revolut.exception.AccountException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.CustomerId;
import pl.com.revolut.model.Customer;

import java.util.List;

public interface CustomerService {

    /**
     *
     * @return all the customers in the store
     */

    List<Customer> getAllCustomers();

    /**
     * adds or updates an customer in the store
     *@param customer which will add if there is any or update if there is a customer which exists
     * @throws NullParameterException if the parameters are null
     */
    void addOrUpdateCustomer(Customer customer) throws NullParameterException;


    /**
     * remove existing customer by {@param customerId}
     * @param customerId which belongs to a customer
     * @return the removed customer which has {@param customerId }
     * @throws NullParameterException if the parameters are null
     * @throws AccountException if there is any account belongs to customerId
     */
    Customer removeCustomer(CustomerId customerId) throws NullParameterException, AccountException;

    /**
     * @param customerId belongs to customer
     * @return a customer which has customer id
     * @throws NullParameterException if the parameters are null
     */
    Customer getCustomerById(CustomerId customerId) throws NullParameterException;


    /**
     * @param customerId belongs to customer
     * @param accountId belongs to customer
     * @return a customer which has customer id
     * @throws NullParameterException if the parameters are null
     */
    Boolean addAccountToCustomer(CustomerId customerId, AccountId accountId) throws  NullParameterException;

    /**
     * remove an account which has got {@param customerId} from the customer
     * @param customerId which belongs to a Customer
     * @param accountId which belongs to an Account will attach to an Customer
     * @return the status of the operation
     * @throws NullParameterException if the parameters are null
     */
    Boolean removeAccountFromCustomer(CustomerId customerId, AccountId accountId) throws  NullParameterException;


    /**
     * @param customerId of the customer which has account
     * @return return the list of accounts belongs to a customer
     * @throws NullParameterException if the parameters are null
     */
    List<AccountId> getCustomerAccounts(CustomerId customerId) throws NullParameterException;

    /**
     * remove all the customers in the store
     * @throws NullParameterException  if the parameters are null
     * @throws AccountException if there is any account belongs to customerId
     */
    void removeAllCustomers() throws NullParameterException, AccountException;

}
