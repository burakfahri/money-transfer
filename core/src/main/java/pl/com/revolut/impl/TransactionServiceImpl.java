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
import java.util.Collections;
import java.util.List;

/**
 * @see TransactionService
 */
public class TransactionServiceImpl extends StorageService<TransactionId, Transaction> implements TransactionService {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TransactionServiceImpl.class);

    private static TransactionService transactionServiceInstance = null;
    private static AccountService accountServiceInstance = null;

    private TransactionServiceImpl() {
        super();
    }

    public static synchronized TransactionService getTransactionServiceInstance() {
        if (transactionServiceInstance == null) {
            transactionServiceInstance = new TransactionServiceImpl();
            logger.info("Transaction service instantiated");

        }
        return transactionServiceInstance;
    }

    public void setAccountService(final AccountService accountService) throws NullParameterException {
        ServiceUtils.checkParameters(accountService);
        logger.info("Account Service setted into Transtaction service instantiated");
        accountServiceInstance = accountService;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        logger.info("returning all transactions");
        return Collections.unmodifiableList(super.getAll());
    }

    public void addOrUpdateTransaction(final Transaction transaction) throws NullParameterException {
        ServiceUtils.checkParameters(transaction);
        logger.info("add or update transaction "+transaction);

        super.addOrUpdateItem(transaction.getTransactionId(), transaction);
    }


    @Override
    public Transaction getTransactionById(final TransactionId transactionId) throws NullParameterException {
        ServiceUtils.checkParameters(transactionId);
        logger.info("fetching all transactions by id "+transactionId);
        return super.getItem(transactionId);
    }

    private Account checkParametersAndReturnAccount(final AccountId accountId,final  BigDecimal amount) throws AccountServiceException,
            NullParameterException, AccountException {

        if (accountServiceInstance == null) {
            logger.error("Account service does not set");
            throw new AccountServiceException();
        }
        ServiceUtils.checkParameters(accountId,amount);
        final Account account = accountServiceInstance.getAccountById(accountId);

        if (account == null) {
            logger.error("Account does not exit "+ account);

            throw new AccountException();
        }
        return account;
    }

    @Override
    public Transaction deposit(final AccountId accountId, final BigDecimal amount) throws NullParameterException, AccountServiceException,
            AccountException, IdException {

        Account account = checkParametersAndReturnAccount(accountId,amount);
        logger.info("deposit for account "+account);

        Transaction transaction = ServiceUtils.createDepositOrWithDrawTransaction(amount, TransactionType.DEPOSIT,accountId);

        addMoneyFromTheAccount(account,transaction.getTransactionId(),amount);

        addOrUpdateTransaction(transaction);
        return transaction;
    }



    private void setCurrentMoneyAndSetAccount(final Account account,final BigDecimal amount) throws NullParameterException {
        account.setCurrentBalance(amount);
        accountServiceInstance.addOrUpdateAccount(account);
    }

    private void addMoneyFromTheAccount (Account account,TransactionId transactionId,BigDecimal amount)
            throws  NullParameterException {

        accountServiceInstance.addTransactionToAccount(account.getAccountId(),transactionId);
        setCurrentMoneyAndSetAccount(account,account.getCurrentBalance().add(amount));
    }

    private void removeMoneyFromTheAccount (final Account account,final TransactionId transactionId,BigDecimal amount)
            throws TransactionException, NullParameterException {

        if (amount.compareTo(account.getCurrentBalance()) > 0) {
            logger.error("ACCOUNT IS NOT ABLE TO WITHDRAW");
            throw new TransactionException("ACCOUNT IS NOT ABLE TO WITHDRAW");
        }
        accountServiceInstance.addTransactionToAccount(account.getAccountId(),transactionId);
        setCurrentMoneyAndSetAccount(account,account.getCurrentBalance().subtract(amount));
    }

    @Override
    public Transaction withDraw(final AccountId accountId,final BigDecimal amount) throws AccountServiceException, NullParameterException,
            AccountException, TransactionException, IdException {

        Account account = checkParametersAndReturnAccount(accountId,amount);

        Transaction transaction = ServiceUtils.createDepositOrWithDrawTransaction(amount, TransactionType.WITHDRAW,accountId);
        logger.info("withdraw for account "+account);

        if(transaction == null) {
            logger.error("transaction could not create");
            throw new TransactionException();
        }
        removeMoneyFromTheAccount(account,transaction.getTransactionId(),amount);

        addOrUpdateTransaction(transaction);
        return transaction;
    }

    @Override
    public Transaction transfer(final AccountId senderAccountId,final AccountId receiverAccountId, BigDecimal amount,String expl)
            throws AccountException, TransactionException, NullParameterException, AccountServiceException, IdException {
        Account receiverAccount = checkParametersAndReturnAccount(receiverAccountId,amount);
        Account senderAccount = checkParametersAndReturnAccount(senderAccountId,amount);

        Transaction transaction = ServiceUtils.createTransferTransaction(amount,senderAccountId,receiverAccountId,expl);

        removeMoneyFromTheAccount(senderAccount,transaction.getTransactionId(),amount);

        addMoneyFromTheAccount(receiverAccount,transaction.getTransactionId(),amount);

        addOrUpdateTransaction(transaction);
        logger.info("transfer for sender account "+senderAccount + " to " + receiverAccount + " amount =  " + amount +
                " explanation is " + expl);

        return transaction;
    }

    public void removeTransaction(final TransactionId transactionId) throws NullParameterException, TransactionException {
        if (transactionId == null)
            throw new NullParameterException();
        Transaction transaction = getTransactionById(transactionId);
        if(transaction == null) {
            logger.error("Transaction does not exist with transactionId "+ transaction );
            throw new TransactionException();
        }
        if(transaction.getReceiverAccountId() != null)
            accountServiceInstance.removeTransactionFromAccount(transactionId,transaction.getReceiverAccountId());
        if(transaction.getSenderAccountId() != null)
            accountServiceInstance.removeTransactionFromAccount(transactionId,transaction.getSenderAccountId());

        logger.info("removing transaction with id " + transactionId);
        super.remove(transactionId);
    }

    @Override
    public void removeAllTransactions() throws NullParameterException, TransactionException {
        logger.info("removing all transactions");
        for (Transaction transaction : super.getAll()) {
            removeTransaction(transaction.getTransactionId());
        }
    }
}
