package pl.com.revolut.impl;

import pl.com.revolut.exception.AccountException;
import pl.com.revolut.common.service.StorageService;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.ServiceUtils;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.TransactionId;
import pl.com.revolut.service.AccountService;
import pl.com.revolut.service.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountServiceImpl extends StorageService<AccountId,Account> implements AccountService {
    private static AccountService accountServiceInstance = null;
    private CustomerService customerService = null;
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AccountServiceImpl.class);
    private final Map<AccountId,List<TransactionId>> accountIdToTransactionId = new ConcurrentHashMap<>();



    private AccountServiceImpl(){
        super();
    }

    public static  synchronized AccountService getAccountServiceInstance(){
        if(accountServiceInstance == null) {
            accountServiceInstance = new AccountServiceImpl();
        }
        return accountServiceInstance;
    }

    public void setCustomerService(CustomerService customerService){
        this.customerService = customerService;
    }

    @Override
    public List<Account> getAllAccounts() {
        return super.getAll();
    }


    @Override
    public void addOrUpdateAccount(Account account) throws NullParameterException {
        ServiceUtils.checkParameters(account);
        Account accountOld = super.getItem(account.getAccountId());
        if(!accountOld.getCustomerId().equals(account.getCustomerId())) {
            customerService.removeAccountFromCustomer(accountOld.getCustomerId(),accountOld.getAccountId());
            customerService.addAccountToCustomer(account.getCustomerId(),account.getAccountId());
        }
        super.addOrUpdateItem(account.getAccountId(),account);
    }

    @Override
    public Account removeAccount(AccountId accountId) throws NullParameterException, AccountException {
        ServiceUtils.checkParameters(accountId);
        Account account = super.getItem(accountId);
        if(account == null)
            throw new AccountException();
        customerService.removeAccountFromCustomer(account.getCustomerId(),account.getAccountId());
        return super.remove(accountId);
    }

    @Override
    public Account getAccountById(AccountId accountId) throws NullParameterException {
        ServiceUtils.checkParameters(accountId);
        return super.getItem(accountId);
    }

    @Override
    public Boolean addTransactionToAccount(AccountId accountId, TransactionId transactionId) throws NullParameterException {
        ServiceUtils.checkParameters(transactionId,accountId);
        List<TransactionId> transactionIdList = accountIdToTransactionId.getOrDefault(accountId,new ArrayList<>());
        if(transactionIdList.contains(transactionId))
            return false;
        transactionIdList.add(transactionId);
        accountIdToTransactionId.put(accountId,transactionIdList);
        return true;
    }

    @Override
    public Boolean removeTransactionFromAccount(TransactionId transactionId, AccountId accountId) throws NullParameterException {
        ServiceUtils.checkParameters(transactionId,accountId);
        List<TransactionId> transactionIdList = accountIdToTransactionId.getOrDefault(accountId,new ArrayList<>());
        if(!transactionIdList.contains(transactionId))
            return false;
        transactionIdList.remove(transactionId);
        accountIdToTransactionId.put(accountId,transactionIdList);
        return true;
    }

    @Override
    public List<TransactionId> getTransactionsOfAccount(AccountId accountId) throws NullParameterException {
        ServiceUtils.checkParameters(accountId);
        return accountIdToTransactionId.getOrDefault(accountId,new ArrayList<>());
    }
}
