package pl.com.revolut.service;

import pl.com.revolut.exception.AccountException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.TransactionId;

import java.util.List;

public interface AccountService {

    /**
     * sets the customer service
     * @param customerService
     */
    void setCustomerService(CustomerService customerService);

    /**
     *
     * @return all the accounts
     */

    List<Account> getAllAccounts();

    /**
     * add or update the account
     */
    void addOrUpdateAccount(Account account) throws NullParameterException;


    /**
     * remove existing account by accountId
     * return true if it can delete
     */
    Account removeAccount(AccountId accountId) throws NullParameterException, AccountException;

    /**
     * @param accountId belongs to Account
     * getAccountById
     * @return account by {@code accountId}
     */
    Account getAccountById(AccountId accountId) throws NullParameterException;

    Boolean addTransactionToAccount(AccountId accountId, TransactionId transactionId) throws NullParameterException;

    Boolean removeTransactionFromAccount(TransactionId transactionId, AccountId accountId) throws NullParameterException;

    List<TransactionId> getTransactionsOfAccount(AccountId accountId) throws NullParameterException, AccountException;

    void removeAllAccounts() throws NullParameterException, AccountException;
}
