package pl.com.revolut.service;

import pl.com.revolut.common.exception.NullParameterException;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.identifier.AccountId;

import java.util.List;

public interface AccountService {

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
    Account removeAccount(AccountId accountId) throws NullParameterException;

    /**
     * @param accountId belongs to Account
     * getAccountById
     * @return account by {@code accountId}
     */
    Account getAccountById(AccountId accountId) throws NullParameterException;

}
