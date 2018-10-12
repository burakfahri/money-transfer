package pl.com.revolut.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

@Service
@Slf4j
public class CustomerServiceImpl extends StorageService<CustomerId,Customer> implements CustomerService  {
    private Map<CustomerId,List<AccountId>> customerIdToAccountId = new ConcurrentHashMap<>();


    @Override
    public synchronized List<Customer> getAllCustomers() {
        log.info("Returning all customers");
        return Collections.unmodifiableList(Collections.unmodifiableList(super.getAll()));
    }

    @Override
    public synchronized void addOrUpdateCustomer(final Customer customer) throws NullParameterException {
        ServiceUtils.checkParameters(customer);
        log.info("Adding or updating customers "+customer);
        customer.setAttendDate(Calendar.getInstance().getTime());
        super.addOrUpdateItem(customer.getCustomerId(),customer);
    }

    @Override
    public synchronized Customer removeCustomer(final CustomerId customerId) throws NullParameterException, AccountException {
        ServiceUtils.checkParameters(customerId);
        log.info("Removing customer with id "+customerId);

        List<AccountId> accountIdList = customerIdToAccountId.get(customerId);
        if(accountIdList != null && !accountIdList.isEmpty()) {
            log.error("User can not delete while it has accounts customerId "+ customerId);
            throw new AccountException("User can not delete while it has accounts");
        }
        return super.remove(customerId) ;
    }

    @Override
    public Customer getCustomerById(final CustomerId customerId) throws NullParameterException {
        ServiceUtils.checkParameters(customerId);
        log.info("Returning customer with  id "+customerId);
        return super.getItem(customerId);
    }

    private Boolean handleAddAccountToCustomer(List<AccountId> accountIdList,final CustomerId customerId, final AccountId accountId)
    {
        accountIdList.add(accountId);
        customerIdToAccountId.put(customerId,accountIdList);
        log.info("Add account to customer cus id "+customerId + " account id "+ accountId);
        return true;
    }

    @Override
    public synchronized Boolean addAccountToCustomer(final CustomerId customerId, final AccountId accountId) throws NullParameterException {
        ServiceUtils.checkParameters(customerId,accountId);
        List<AccountId> accountIdList = customerIdToAccountId.getOrDefault(customerId,new ArrayList<>());
        return !ServiceUtils.checkListContain(accountIdList,accountId)
                && handleAddAccountToCustomer(accountIdList,customerId,accountId);
    }



    private Boolean handleRemoveAccountFromCustomer(List<AccountId> accountIdList,final CustomerId customerId,
                                                 final AccountId accountId){
        accountIdList.remove(accountId);
        customerIdToAccountId.put(customerId,accountIdList);
        log.info("removing account "+ customerId);
        return true;
    }


    @Override
    public synchronized Boolean removeAccountFromCustomer(final CustomerId customerId, final AccountId accountId) throws NullParameterException {
        ServiceUtils.checkParameters(customerId,accountId);
        List<AccountId> accountIdList = getCustomerAccounts(customerId);
        return ServiceUtils.checkListContain(accountIdList,accountId) &&
                handleRemoveAccountFromCustomer(accountIdList,customerId,accountId);
    }

    @Override
    public synchronized List<AccountId> getCustomerAccounts(final CustomerId customerId) throws NullParameterException {
        log.info("fetching customer of accounts "+ customerId);
        ServiceUtils.checkParameters(customerId);
        return customerIdToAccountId.getOrDefault(customerId,new ArrayList<>());
    }

    @Override
    public synchronized  void removeAllCustomers() throws NullParameterException, AccountException {
        log.info("removing all customers");

        for (Customer customer : super.getAll()) {
            removeCustomer(customer.getCustomerId());
        }
    }
}
