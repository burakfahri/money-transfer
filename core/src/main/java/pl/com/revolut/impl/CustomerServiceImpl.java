package pl.com.revolut.impl;

import pl.com.revolut.common.service.StorageService;
import pl.com.revolut.exception.AccountException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.ServiceUtils;
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.CustomerId;
import pl.com.revolut.service.CustomerService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see CustomerService
 */
public class CustomerServiceImpl extends StorageService<CustomerId,Customer> implements CustomerService  {
    private static CustomerService customerServiceInstance = null;
    private Map<CustomerId,List<AccountId>> customerIdToAccountId = new ConcurrentHashMap<>();
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CustomerService.class);

    private CustomerServiceImpl() {
        super();
    }

    public synchronized static CustomerService getCustomerServiceInstance()
    {
        if(customerServiceInstance == null) {
            customerServiceInstance = new CustomerServiceImpl();
            logger.info("Customer service instantiated");

        }
        return customerServiceInstance;
    }

    @Override
    public List<Customer> getAllCustomers() {
        logger.info("Returning all customers");
        return Collections.unmodifiableList(Collections.unmodifiableList(super.getAll()));
    }

    @Override
    public void addOrUpdateCustomer(final Customer customer) throws NullParameterException {
        ServiceUtils.checkParameters(customer);
        logger.info("Adding or updating customers "+customer);
        customer.setAttendDate(Calendar.getInstance().getTime());
        super.addOrUpdateItem(customer.getCustomerId(),customer);
    }

    @Override
    public Customer removeCustomer(final CustomerId customerId) throws NullParameterException, AccountException {
        ServiceUtils.checkParameters(customerId);
        logger.info("Removing customer with id "+customerId);

        List<AccountId> accountIdList = customerIdToAccountId.get(customerId);
        if(accountIdList != null && !accountIdList.isEmpty()) {
            logger.error("User can not delete while it has accounts customerId "+ customerId);
            throw new AccountException("User can not delete while it has accounts");
        }
        return super.remove(customerId) ;
    }

    @Override
    public Customer getCustomerById(final CustomerId customerId) throws NullParameterException {
        ServiceUtils.checkParameters(customerId);
        logger.info("Returning customer with  id "+customerId);
        return super.getItem(customerId);
    }

    @Override
    public Boolean addAccountToCustomer(final CustomerId customerId, final AccountId accountId) throws NullParameterException {
        ServiceUtils.checkParameters(customerId,accountId);
        List<AccountId> accountIdList = customerIdToAccountId.getOrDefault(customerId,new ArrayList<>());
        if(accountIdList.contains(accountId)) {
            logger.error("Account already exist for customer id = "+customerId);
            return false;
        }
        accountIdList.add(accountId);
        customerIdToAccountId.put(customerId,accountIdList);
        logger.info("Add account to customer cus id "+customerId + " account id "+ accountId);

        return true;
    }

    @Override
    public Boolean removeAccountFromCustomer(final CustomerId customerId, final AccountId accountId) throws NullParameterException {
        ServiceUtils.checkParameters(customerId,accountId);
        List<AccountId> accountIdList = customerIdToAccountId.getOrDefault(customerId,new ArrayList<>());
        if(!accountIdList.contains(accountId)) {
            logger.error("Account does not exist for customer id = "+customerId + " account id "+ accountId);
            return false;
        }
        accountIdList.remove(accountId);
        customerIdToAccountId.put(customerId,accountIdList);
        logger.info("removing account "+ customerId);
        return true;
    }

    @Override
    public List<AccountId> getCustomerAccounts(final CustomerId customerId) throws NullParameterException {
        logger.info("fetching customer of accounts "+ customerId);
        ServiceUtils.checkParameters(customerId);
        return customerIdToAccountId.getOrDefault(customerId,new ArrayList<>());
    }

    @Override
    public void removeAllCustomers() throws NullParameterException, AccountException {
        logger.info("removing all customers");

        for (Customer customer : super.getAll()) {
            removeCustomer(customer.getCustomerId());
        }
    }
}
