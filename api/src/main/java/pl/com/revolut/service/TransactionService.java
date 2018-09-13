package pl.com.revolut.service;

import pl.com.revolut.exception.*;
import pl.com.revolut.model.Transaction;
import pl.com.revolut.model.identifier.TransactionId;
import pl.com.revolut.model.identifier.AccountId;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    /**
     * Account service must use when transaction issues.
     * @param accountService using for injection
     */
    void setAccountService(AccountService accountService);

    /**
     *
     * @return all the transactions
     */

    List<Transaction> getAllTransactions();


    /**
     * @param transactionId belongs to transaction
     * @return transaction which has {@code transactionId}
     * @throws NullParameterException if the parameters is null
     */
    Transaction getTransactionById(TransactionId transactionId)
            throws  NullParameterException;


    /**
     *
     * @param accountId of Account which would use for deposit
     * @param amount which will deposit to an account
     * @return the transaction which produce from the operation
     * @throws AccountServiceException if there is any account belongs to {@param accountId}
     * @throws NullParameterException if the parameters are null
     * @throws AccountException if there is not any account belongs to {@param accountId}
     * @throws IdException if the account id is not valid
     */
    Transaction deposit(AccountId accountId, BigDecimal amount)
            throws NullParameterException, AccountServiceException, AccountException, IdException;


    /**
     *
     * @param accountId of Account which would use for withdraw
     * @param amount which will withdraw from an account
     * @return the transaction which produce from the operation
     * @throws AccountServiceException if there is any account belongs to {@param accountId}
     * @throws NullParameterException if the parameters are null
     * @throws AccountException if there is not any account belongs to {@param accountId}
     * @throws TransactionException if the owner of the account does not have enough money to withdraw {@param amount}
     * @throws IdException if the account id is not valid
     */
    Transaction withDraw(AccountId accountId, BigDecimal amount)
            throws AccountServiceException, NullParameterException, AccountException, TransactionException, IdException;


    /**
     *
     * @param senderAccountId sender acount id
     * @param receiverAccountId receiver acount id
     * @param amount of the money which would be transfered.
     * @param expl which describe the details of the transfer optional
     * @return the transaction of the transfer operation
     * @throws AccountException if there is any account belongs to {@param senderAccountId} or {@param receiverAccountId}
     * @throws TransactionException if the sender which has {@param senderAccountId} does not have enough money to send {@param amount
     * @throws NullParameterException if the parameters are null
     * @throws AccountServiceException if there is not any account belongs to {@param senderAccountId} or {@param receiverAccountId}
     * @throws IdException if the id is not valid
     */
    Transaction transfer(AccountId senderAccountId, AccountId receiverAccountId, BigDecimal amount,String expl)
            throws AccountException, TransactionException, NullParameterException, AccountServiceException, IdException;

    /**
     * remove all the transactions in the store
     * @throws NullParameterException if the parameters are null
     * @throws TransactionException if the transaction ids does not belongs to any transaction is the store
     */
    void removeAllTransactions() throws NullParameterException, TransactionException;

}
