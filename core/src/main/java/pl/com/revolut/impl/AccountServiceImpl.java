package pl.com.revolut.impl;

import pl.com.revolut.common.service.StorageService;
import pl.com.revolut.common.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.ServiceImplUtils;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.service.AccountService;

import java.util.List;

public class AccountServiceImpl extends StorageService<AccountId,Account> implements AccountService {
    private static AccountService accountServiceInstance = null;
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AccountServiceImpl.class);

    private AccountServiceImpl(){
        super();
    }

    public static  synchronized AccountService getAccountServiceInstance(){
        if(accountServiceInstance == null) {
            accountServiceInstance = new AccountServiceImpl();
        }
        return accountServiceInstance;
    }

    @Override
    public List<Account> getAllAccounts() {
        return super.getAll();
    }


    @Override
    public void addOrUpdateAccount(Account account) throws NullParameterException {
        ServiceImplUtils.checkParameters(account);
        super.addOrUpdateItem(account.getAccountId(),account);
    }

    @Override
    public Account removeAccount(AccountId accountId) throws NullParameterException {
        ServiceImplUtils.checkParameters(accountId);
        return super.remove(accountId);
    }

    @Override
    public Account getAccountById(AccountId accountId) throws NullParameterException {
        ServiceImplUtils.checkParameters(accountId);
        return super.getItem(accountId);
    }
}
