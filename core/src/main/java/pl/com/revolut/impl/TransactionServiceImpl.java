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
    public Transaction deposit(AccountId accountId, BigDecimal amount) throws NullParameterException, AccountServiceException,
            AccountException, IdException {

        Account account = checkParametersAndReturnAccount(accountId,amount);

        Transaction transaction = ServiceUtils.createDepositOrWithDrawTransaction(amount, TransactionType.DEPOSIT,accountId);

        addMoneyFromTheAccount(account,transaction.getTransactionId(),amount);

        addOrUpdateTransaction(transaction);
        return transaction;
    }



    private void setCurrentMoneyAndSetAccount(Account account,BigDecimal amount) throws NullParameterException {
        account.setCurrentBalance(amount);
        accountServiceInstance.addOrUpdateAccount(account);
    }

    private void addMoneyFromTheAccount (Account account,TransactionId transactionId,BigDecimal amount)
            throws  NullParameterException {

        accountServiceInstance.addTransactionToAccount(account.getAccountId(),transactionId);
        setCurrentMoneyAndSetAccount(account,account.getCurrentBalance().add(amount));
    }

    private void removeMoneyFromTheAccount (Account account,TransactionId transactionId,BigDecimal amount)
            throws TransactionException, NullParameterException {

        if (amount.compareTo(account.getCurrentBalance()) > 0) {
            throw new TransactionException("ACCOUNT IS NOT ABLE TO WITHDRAW");
        }
        accountServiceInstance.addTransactionToAccount(account.getAccountId(),transactionId);
        setCurrentMoneyAndSetAccount(account,account.getCurrentBalance().subtract(amount));
    }

    @Override
    public Transaction withDraw(AccountId accountId, BigDecimal amount) throws AccountServiceException, NullParameterException,
            AccountException, TransactionException, IdException {

        Account account = checkParametersAndReturnAccount(accountId,amount);

        Transaction transaction = ServiceUtils.createDepositOrWithDrawTransaction(amount, TransactionType.WITHDRAW,accountId);


        removeMoneyFromTheAccount(account,transaction.getTransactionId(),amount);

        addOrUpdateTransaction(transaction);
        return transaction;
    }

    @Override
    public Transaction transfer(AccountId senderAccountId, AccountId receiverAccountId, BigDecimal amount,String expl) throws AccountException, TransactionException, NullParameterException, AccountServiceException, IdException {
        Account receiverAccount = checkParametersAndReturnAccount(receiverAccountId,amount);
        Account senderAccount = checkParametersAndReturnAccount(senderAccountId,amount);

        Transaction transaction = ServiceUtils.createTransferTransaction(amount,senderAccountId,receiverAccountId,expl);

        removeMoneyFromTheAccount(senderAccount,transaction.getTransactionId(),amount);

        addMoneyFromTheAccount(receiverAccount,transaction.getTransactionId(),amount);

        addOrUpdateTransaction(transaction);

        return transaction;
    }
}
