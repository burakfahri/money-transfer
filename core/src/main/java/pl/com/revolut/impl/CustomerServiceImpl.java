package pl.com.revolut.impl;

import pl.com.revolut.common.service.StorageService;
import pl.com.revolut.exception.AccountException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.ServiceUtils;
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.CustomerId;
import pl.com.revolut.service.CustomerService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CustomerServiceImpl extends StorageService<CustomerId,Customer> implements CustomerService  {
    private static CustomerService customerServiceInstance = null;
    private Map<CustomerId,List<AccountId>> customerIdToAccountId = new ConcurrentHashMap<>();

    private CustomerServiceImpl() {
        super();
    }

    public synchronized static CustomerService getCustomerServiceInstance()
    {
        if(customerServiceInstance == null)
            customerServiceInstance = new CustomerServiceImpl();
        return customerServiceInstance;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return super.getAll();
    }

    @Override
    public void addOrUpdateCustomer(Customer customer) throws NullParameterException {
        ServiceUtils.checkParameters(customer);
        customer.setAttendDate(Calendar.getInstance().getTime());
        super.addOrUpdateItem(customer.getCustomerId(),customer);
    }

    @Override
    public Customer removeCustomer(CustomerId customerId) throws NullParameterException, AccountException {
        ServiceUtils.checkParameters(customerId);
        List<AccountId> accountIdList = customerIdToAccountId.get(customerId);
        if(accountIdList != null && !accountIdList.isEmpty())
            throw new AccountException("User can not delete while it has accounts");
        return super.remove(customerId) ;
    }

    @Override
    public Customer getCustomerById(CustomerId customerId) throws NullParameterException {
        ServiceUtils.checkParameters(customerId);
        return super.getItem(customerId);
    }

    @Override
    public Boolean addAccountToCustomer(CustomerId customerId, AccountId accountId) throws NullParameterException {
        ServiceUtils.checkParameters(customerId,accountId);
        List<AccountId> accountIdList = customerIdToAccountId.getOrDefault(customerId,new ArrayList<>());
        if(accountIdList.contains(accountId))
            return false;
        accountIdList.add(accountId);
        customerIdToAccountId.put(customerId,accountIdList);
        return true;
    }

    @Override
    public Boolean removeAccountFromCustomer(CustomerId customerId, AccountId accountId) throws NullParameterException {
        ServiceUtils.checkParameters(customerId,accountId);
        List<AccountId> accountIdList = customerIdToAccountId.getOrDefault(customerId,new ArrayList<>());
        if(!accountIdList.contains(accountId))
            return false;
        accountIdList.remove(accountId);
        customerIdToAccountId.put(customerId,accountIdList);
        return true;
    }

    @Override
    public List<AccountId> getCustomerAccounts(CustomerId customerId) throws NullParameterException {
        ServiceUtils.checkParameters(customerId);
        return customerIdToAccountId.getOrDefault(customerId,new ArrayList<>());
    }

    @Override
    public void removeAllCustomers() throws NullParameterException, AccountException {
        for (Customer customer : super.getAll()) {
            removeCustomer(customer.getCustomerId());
        }
    }
}
