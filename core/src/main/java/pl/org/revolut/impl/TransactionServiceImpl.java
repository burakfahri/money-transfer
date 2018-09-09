package pl.org.revolut.impl;

import pl.org.revolut.app.impl.StorageService;
import pl.org.revolut.exception.*;
import pl.org.revolut.model.Account;
import pl.org.revolut.model.Transaction;
import pl.org.revolut.model.identifier.AccountId;
import pl.org.revolut.model.identifier.TransactionId;
import pl.org.revolut.service.AccountService;
import pl.org.revolut.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;

public class TransactionServiceImpl extends StorageService<TransactionId,Transaction> implements TransactionService{

    private static TransactionService transactionServiceInstance = null;
    private static AccountService accountServiceImpl = null;

    private TransactionServiceImpl(){super();}

    public static synchronized TransactionService  getTransactionService()
    {
        if(transactionServiceInstance == null)
            transactionServiceInstance = new TransactionServiceImpl();
        return transactionServiceInstance;
    }

    public void setAccountService(AccountService accountService)
    {
        if(accountService != null)
            accountServiceImpl = accountService;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return super.getAll();
    }

    @Override
    public void addOrUpdateTransaction(Transaction transaction) throws NullParameterException {
        if(transaction == null)
            throw  new NullParameterException("TRANSACTION PARAMETER IS NULL");
            super.addOrUpdateItem(transaction.getTransactionId(),transaction);
    }

    @Override
    public Boolean removeTransaction(TransactionId transactionId) throws NullParameterException {
        if(transactionId == null)
            throw  new NullParameterException();
        return super.remove(transactionId) != null;
    }

    @Override
    public Transaction getTransactionById(TransactionId transactionId) throws NullParameterException {
        if(transactionId == null)
            throw  new NullParameterException();
        return super.getItem(transactionId);
    }

    @Override
    public void deposit(AccountId accountId, BigDecimal amounth) throws NullParameterException, AccountServiceException, AccountException {
        if(accountServiceImpl == null)
            throw  new AccountServiceException();
        if(accountId == null || amounth == null)
            throw new NullParameterException();
        final Account account = accountServiceImpl.getAccountById(accountId);
        if(account == null)
            throw new AccountException();

        account.setCurrentBalance(account.getCurrentBalance().add(amounth));
        accountServiceImpl.addOrUpdateAccount(account);
    }

    @Override
    public void withDraw(AccountId accountId, BigDecimal amounth) throws AccountServiceException, NullParameterException,
            AccountException, TransactionException {

        if(accountServiceImpl == null)
            throw  new AccountServiceException();
        if(accountId == null || amounth == null)
            throw new NullParameterException();
        final Account account = accountServiceImpl.getAccountById(accountId);
        if(account == null)
            throw new AccountException();
        if(amounth.compareTo(account.getCurrentBalance()) < 0)
            throw  new TransactionException("ACCOUNT IS NOT ABLE TO WITHDRAW");

        account.setCurrentBalance(account.getCurrentBalance().subtract(amounth));
        accountServiceImpl.addOrUpdateAccount(account);
    }

    @Override
    public void transfer(AccountId senderAcountId, AccountId receiverAccountId, BigDecimal amounth) throws AccountException, TransactionException, NullParameterException, AccountServiceException {
        try {
            withDraw(senderAcountId,amounth);
            deposit(receiverAccountId,amounth);
        } catch (Exception e) {
            throw e;
        }
    }
}
