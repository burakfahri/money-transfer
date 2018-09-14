package pl.com.revolut.service;

import pl.com.revolut.exception.AccountException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.TransactionId;

import java.util.List;

public interface AccountService {

    /**
     * sets the customer service to the account service to attach account to customer
     * @param customerService which stores customers
     */
    void setCustomerService(CustomerService customerService) throws NullParameterException;

    /**
     * @return all the accounts in the store
     */
    List<Account> getAllAccounts();

    /**
     * adds or updates an account in the store
     * @param account which will add or update on the store
     * @throws NullParameterException if the parameters are null
     */

    void addOrUpdateAccount(Account account) throws NullParameterException;


    /**
     *
     * @param accountId belongs to Account
     * @return the removed account which has {@param accountId}
     * @throws NullParameterException if the parameters are null
     * @throws AccountException if the {@param accountId} does not belongs to any account
     */

    Account removeAccount(AccountId accountId) throws NullParameterException, AccountException;

    /**
     *
     * @param accountId belongs to Account
     * @return the account which has {@param accountId}
     * @throws NullParameterException if the parameters are null
     */
    Account getAccountById(AccountId accountId) throws NullParameterException;

    /**
     * attach transaction to an account
     * @param accountId of the account which transaction would be attached
     * @param transactionId of the transaction
     * @return the operation status
     * @throws NullParameterException if the parameters are null
     */
    Boolean addTransactionToAccount(AccountId accountId, TransactionId transactionId) throws NullParameterException;


    /**
     * de-attach transaction to an account
     * @param transactionId of the transaction
     * @param accountId of the account which transaction would be de-attached
     * @return the operation status
     * @throws NullParameterException if the parameters are null
     */
    Boolean removeTransactionFromAccount(TransactionId transactionId, AccountId accountId) throws NullParameterException;

    /**
     *
     * @param accountId  of the account which has transactions
     * @return the list of transactions which are attached to account of the {@param accountId}
     * @throws NullParameterException if the parameters are null
     * @throws AccountException if the {@param accountId} does not belongs to any account
     */
    List<TransactionId> getTransactionsOfAccount(AccountId accountId) throws NullParameterException, AccountException;


    /**
     * removes all the accounts in the store
     * also removes the account and transaction links
     * @throws NullParameterException if the parameters are null
     * @throws AccountException if the there is any accountId does not belongs to any account in the store
     */
    void removeAllAccounts() throws NullParameterException, AccountException;
}
