package pl.com.revolut.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.revolut.exception.AccountException;
import pl.com.revolut.common.service.StorageService;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.ServiceUtils;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.Transaction;
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

@Service
@Slf4j
public class AccountServiceImpl extends StorageService<AccountId, Account> implements AccountService {
    private final Map<AccountId, List<TransactionId>> accountIdToTransactionId = new ConcurrentHashMap<>();
    private CustomerService customerService = null;

    @Autowired
    AccountServiceImpl(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    @Override
    public synchronized List<Account> getAllAccounts() {
        log.info("returning all accounts");
        return Collections.unmodifiableList(super.getAll());
    }


    @Override
    public synchronized void addOrUpdateAccount(final Account account) throws NullParameterException {
        ServiceUtils.checkParameters(account);
        customerService.addAccountToCustomer(account.getCustomerId(), account.getAccountId());
        log.info("add or update account " + account);

        super.addOrUpdateItem(account.getAccountId(), account);
    }

    private Account getAccount(AccountId accountId) throws NullParameterException, AccountException {
        Account account = super.getItem(accountId);
        if (account == null) {
            log.error("account id " + accountId + " does not belongs to any account");
            throw new AccountException();
        }
        return account;
    }
    private Account handleRemoveAccount(Account account) throws NullParameterException {
        accountIdToTransactionId.remove(account.getAccountId());
        customerService.removeAccountFromCustomer(account.getCustomerId(), account.getAccountId());
        return super.remove(account.getAccountId());
    }
    @Override
    public synchronized Account removeAccount(final AccountId accountId) throws NullParameterException, AccountException {
        ServiceUtils.checkParameters(accountId);
        Account account = getAccount(accountId);
        log.info("remove account " + account);
        return handleRemoveAccount(account);
    }

    @Override
    public synchronized Account getAccountById(final AccountId accountId) throws NullParameterException {
        ServiceUtils.checkParameters(accountId);
        log.info("account wanted for " + accountId);
        return super.getItem(accountId);
    }

    public boolean addTransactionToAccount(List<TransactionId> transactionIdList, TransactionId transactionId ,
                                               AccountId accountId){

        transactionIdList.add(transactionId);
        accountIdToTransactionId.put(accountId, transactionIdList);
        log.info("transaction added to account with transaction id =  " + transactionId + " account id = " + accountId);
        return true;
    }

    @Override
    public synchronized Boolean addTransactionToAccount(final AccountId accountId, TransactionId transactionId)
            throws NullParameterException, AccountException {
        ServiceUtils.checkParameters(transactionId, accountId);
        List<TransactionId> transactionIdList = getTransactionsOfAccount(accountId);

        return !ServiceUtils.checkListContain(transactionIdList,transactionId) &&
                addTransactionToAccount(transactionIdList,transactionId,accountId);
    }

    private boolean handleRemoveTransactionFromAccount(List<TransactionId> transactionIdList ,
                                                       final TransactionId transactionId, AccountId accountId) {
        transactionIdList.remove(transactionId);
        accountIdToTransactionId.put(accountId, transactionIdList);
        return true;
    }

    @Override
    public synchronized Boolean removeTransactionFromAccount(final TransactionId transactionId, final AccountId accountId)
            throws NullParameterException, AccountException {

        ServiceUtils.checkParameters(transactionId, accountId);
        log.info("transaction removing for transactionId = " + transactionId);
        List<TransactionId> transactionIdList =  getTransactionsOfAccount(accountId);
        return ServiceUtils.checkListContain(transactionIdList,transactionId) &&
                handleRemoveTransactionFromAccount(transactionIdList,transactionId,accountId);
    }

    @Override
    public synchronized List<TransactionId> getTransactionsOfAccount(final AccountId accountId) throws NullParameterException, AccountException {
        ServiceUtils.checkParameters(accountId);
        final Account account = getAccountById(accountId);
        if (account == null) {
            log.error("Account does not exist for transaction id = " + accountId);
            throw new AccountException();
        }
        return accountIdToTransactionId.getOrDefault(accountId, new ArrayList<>());
    }

    @Override
    public synchronized void removeAllAccounts() throws NullParameterException, AccountException {
        log.debug("removing all accounts");
        for (Account account : super.getAll()) {
            removeAccount(account.getAccountId());
        }
    }
}
