package pl.org.revolut.service;

import pl.org.revolut.exception.NullParameterException;
import pl.org.revolut.model.Account;
import pl.org.revolut.model.identifier.AccountId;

import java.math.BigDecimal;
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
    Boolean removeAccount(AccountId accountId) throws NullParameterException;

    /**
     * @param accountId belongs to Account
     * getAccountById
     * @return account by {@code accountId}
     */
    Account getAccountById(AccountId accountId) throws NullParameterException;

}
