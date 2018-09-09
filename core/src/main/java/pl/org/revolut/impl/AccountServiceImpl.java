package pl.org.revolut.impl;

import pl.org.revolut.app.impl.StorageService;
import pl.org.revolut.exception.NullParameterException;
import pl.org.revolut.model.Account;
import pl.org.revolut.model.identifier.AccountId;
import pl.org.revolut.service.AccountService;

import java.util.List;

public class AccountServiceImpl extends StorageService<AccountId,Account> implements AccountService{
    private static AccountService accountServiceInstance = null;

    private AccountServiceImpl(){
        super();
    }

    public static  synchronized AccountService getAccountServiceInstance(){
        if(accountServiceInstance == null)
            accountServiceInstance = new AccountServiceImpl();
        return accountServiceInstance;
    }

    @Override
    public List<Account> getAllAccounts() {
        return super.getAll();
    }

    @Override
    public void addOrUpdateAccount(Account account) throws NullParameterException {
        if(account == null)
            throw new NullParameterException("ACCOUNT IS NULL");
        super.addOrUpdateItem(account.getAccountId(),account);
    }

    @Override
    public Boolean removeAccount(AccountId accountId) throws NullParameterException {
        if(accountId == null)
            throw new NullParameterException("ACCOUNT ID IS NULL");
        return super.remove(accountId) != null;
    }

    @Override
    public Account getAccountById(AccountId accountId) throws NullParameterException {
        if(accountId == null)
            throw new NullParameterException("ACCOUNT ID IS NULL");
        return super.getItem(accountId);
    }
}
