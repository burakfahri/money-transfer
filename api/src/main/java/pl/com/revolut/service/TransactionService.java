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
     * gettransactionById
     * @return transaction by {@code transactionId}
     */
    Transaction getTransactionById(TransactionId transactionId)
            throws  NullParameterException;


    /**
     *
     * @param accountId of Account which would use for deposits
     * @param amount of for deposit
     */
    Transaction deposit(AccountId accountId, BigDecimal amount)
            throws NullParameterException, AccountServiceException, AccountException, IdException;

    /**
     *
     * @param accountId of Account which would use for withdraw
     * @param amount of for amount
     */
    Transaction withDraw(AccountId accountId, BigDecimal amount)
            throws AccountServiceException, NullParameterException, AccountException, TransactionException, IdException;

    /**
     * money transfer btw two accounts
     * @param senderAccountId sender acount id
     * @param receiverAccountId receiver acount id
     * @param amount of the money which would be transfered.
     */
    Transaction transfer(AccountId senderAccountId, AccountId receiverAccountId, BigDecimal amount,String expl)
            throws AccountException, TransactionException, NullParameterException, AccountServiceException, IdException;

    void removeAllTransactions() throws NullParameterException, TransactionException;

}
