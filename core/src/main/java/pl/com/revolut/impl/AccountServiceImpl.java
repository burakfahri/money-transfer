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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see AccountService
 */
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
            logger.info("Account service instantiated");
        }

        return accountServiceInstance;
    }

    public void setCustomerService(CustomerService customerService) throws NullParameterException {
        logger.info("Customer service setted into account service instantiated");
        ServiceUtils.checkParameters(customerService);
        this.customerService = customerService;
    }

    @Override
    public List<Account> getAllAccounts() {
        logger.info("returning all accounts");
        return Collections.unmodifiableList(super.getAll());
    }


    @Override
    public void addOrUpdateAccount(final Account account) throws NullParameterException {
        ServiceUtils.checkParameters(account);
        customerService.addAccountToCustomer(account.getCustomerId(),account.getAccountId());
        logger.info("add or update account "+account);

        super.addOrUpdateItem(account.getAccountId(),account);
    }

    @Override
    public Account removeAccount(final AccountId accountId) throws NullParameterException, AccountException {
        ServiceUtils.checkParameters(accountId);
        Account account = super.getItem(accountId);
        if(account == null) {
            logger.error("account id "+ accountId + " does not belongs to any account");
            throw new AccountException();
        }
        logger.info("remove account "+account);

        accountIdToTransactionId.remove(accountId);
        customerService.removeAccountFromCustomer(account.getCustomerId(),account.getAccountId());
        return super.remove(accountId);
    }

    @Override
    public Account getAccountById(final AccountId accountId) throws NullParameterException {
        ServiceUtils.checkParameters(accountId);
        logger.info("account wanted for "+accountId);

        return super.getItem(accountId);
    }

    @Override
    public Boolean addTransactionToAccount(final AccountId accountId, TransactionId transactionId) throws NullParameterException {
        ServiceUtils.checkParameters(transactionId,accountId);
        List<TransactionId> transactionIdList = accountIdToTransactionId.getOrDefault(accountId,new ArrayList<>());
        if(transactionIdList.contains(transactionId)) {
            logger.error("transaction already exist for transaction id = "+transactionId);
            return false;
        }
        transactionIdList.add(transactionId);
        accountIdToTransactionId.put(accountId,transactionIdList);
        logger.info("transaction added to account with transaction id =  "+transactionId +" account id = "+accountId);

        return true;
    }

    @Override
    public Boolean removeTransactionFromAccount(final TransactionId transactionId, AccountId accountId) throws NullParameterException {
        ServiceUtils.checkParameters(transactionId,accountId);
        List<TransactionId> transactionIdList = accountIdToTransactionId.getOrDefault(accountId,new ArrayList<>());
        if(!transactionIdList.contains(transactionId)) {
            logger.error("transaction does not exist for transaction id = "+transactionId);
            return false;
        }
        transactionIdList.remove(transactionId);
        accountIdToTransactionId.put(accountId,transactionIdList);
        logger.info("transaction removed for transactionId = "+transactionId);
        return true;
    }

    @Override
    public List<TransactionId> getTransactionsOfAccount(final AccountId accountId) throws NullParameterException, AccountException {
        ServiceUtils.checkParameters(accountId);
        final Account account = getAccountById(accountId);
        if(account == null) {
            logger.error("Account does not exist for transaction id = "+accountId);
            throw new AccountException();
        }
        return accountIdToTransactionId.getOrDefault(accountId,new ArrayList<>());
    }

    @Override
    public void removeAllAccounts() throws NullParameterException, AccountException {
        logger.debug("removing all accounts");
        for (Account account : super.getAll()) {
            removeAccount(account.getAccountId());
        }
    }
}
