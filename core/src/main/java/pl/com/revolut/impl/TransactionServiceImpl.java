package pl.com.revolut.impl;

import pl.com.revolut.exception.*;
import pl.com.revolut.common.service.StorageService;
import pl.com.revolut.common.utils.impl.ServiceUtils;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.Transaction;
import pl.com.revolut.model.TransactionType;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.TransactionId;
import pl.com.revolut.service.AccountService;
import pl.com.revolut.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;

public class TransactionServiceImpl extends StorageService<TransactionId, Transaction> implements TransactionService {

    private static TransactionService transactionServiceInstance = null;
    private static AccountService accountServiceInstance = null;

    private TransactionServiceImpl() {
        super();
    }

    public static synchronized TransactionService getTransactionServiceInstance() {
        if (transactionServiceInstance == null)
            transactionServiceInstance = new TransactionServiceImpl();
        return transactionServiceInstance;
    }

    public void setAccountService(AccountService accountService) {
        if (accountService != null)
            accountServiceInstance = accountService;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return super.getAll();
    }

    public void addOrUpdateTransaction(Transaction transaction) throws NullParameterException {
        ServiceUtils.checkParameters(transaction);
        super.addOrUpdateItem(transaction.getTransactionId(), transaction);
    }

    public Boolean removeTransaction(TransactionId transactionId) throws NullParameterException {
        ServiceUtils.checkParameters(transactionId);
        return super.remove(transactionId) != null;
    }

    @Override
    public Transaction getTransactionById(TransactionId transactionId) throws NullParameterException {
        ServiceUtils.checkParameters(transactionId);
        return super.getItem(transactionId);
    }

    private Account checkParametersAndReturnAccount(AccountId accountId, BigDecimal amount) throws AccountServiceException,
            NullParameterException, AccountException {

        if (accountServiceInstance == null)
            throw new AccountServiceException();

        ServiceUtils.checkParameters(accountId,amount);
        final Account account = accountServiceInstance.getAccountById(accountId);

        if (account == null)
            throw new AccountException();
        return account;
    }

    @Override
    public void deposit(AccountId accountId, BigDecimal amount) throws NullParameterException, AccountServiceException,
            AccountException, IdException {

        Account account = checkParametersAndReturnAccount(accountId,amount);

        Transaction transaction = ServiceUtils.createDepositOrWithDrawTransaction(amount, TransactionType.DEPOSIT,accountId);

        addOrUpdateTransaction(transaction);
        accountServiceInstance.addTransactionToAccount(accountId, transaction.getTransactionId());

        setCurrentMoneyAndSetAccount(account,amount);
    }

    private void setCurrentMoneyAndSetAccount(Account account,BigDecimal amount) throws NullParameterException {
        account.setCurrentBalance(account.getCurrentBalance().add(amount));
        accountServiceInstance.addOrUpdateAccount(account);
    }

    private void removeMoneyFromTheAccount (Account account,TransactionId transactionId,BigDecimal amount) throws TransactionException, NullParameterException {

        if (amount.compareTo(account.getCurrentBalance()) < 0) {
            throw new TransactionException("ACCOUNT IS NOT ABLE TO WITHDRAW");
        }
        accountServiceInstance.removeTransactionFromAccount(transactionId,account.getAccountId());
        setCurrentMoneyAndSetAccount(account,amount);
    }

    @Override
    public void withDraw(AccountId accountId, BigDecimal amount) throws AccountServiceException, NullParameterException,
            AccountException, TransactionException, IdException {

        Account account = checkParametersAndReturnAccount(accountId,amount);

        Transaction transaction = ServiceUtils.createDepositOrWithDrawTransaction(amount, TransactionType.WITHDRAW,accountId);


        removeMoneyFromTheAccount(account,transaction.getTransactionId(),amount);

        addOrUpdateTransaction(transaction);
    }

    @Override
    public void transfer(AccountId senderAccountId, AccountId receiverAccountId, BigDecimal amount,String expl) throws AccountException, TransactionException, NullParameterException, AccountServiceException, IdException {
        Account receiverAccount = checkParametersAndReturnAccount(receiverAccountId,amount);
        Account senderAccount = checkParametersAndReturnAccount(senderAccountId,amount);

        Transaction transaction = ServiceUtils.createTransferTransaction(amount,senderAccountId,receiverAccountId,expl);

        removeMoneyFromTheAccount(senderAccount,transaction.getTransactionId(),amount);


        receiverAccount.setCurrentBalance(receiverAccount.getCurrentBalance().add(amount));
        setCurrentMoneyAndSetAccount(receiverAccount,amount);
    }
}
