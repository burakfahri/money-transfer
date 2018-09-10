package pl.com.revolut.service;

import pl.com.revolut.common.exception.AccountException;
import pl.com.revolut.common.exception.AccountServiceException;
import pl.com.revolut.common.exception.NullParameterException;
import pl.com.revolut.common.exception.TransactionException;
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
     * add or update the transaction
     */
    void addOrUpdateTransaction(Transaction transaction) throws NullParameterException;


    /**
     * remove existing transaction by transactionId
     * return true if it can delete
     */
    Boolean removeTransaction(TransactionId transactionId) throws NullParameterException;

    /**
     * @param transactionId belongs to transaction
     * gettransactionById
     * @return transaction by {@code transactionId}
     */
    Transaction getTransactionById(TransactionId transactionId) throws  NullParameterException;


    /**
     *
     * @param accountId of Account which would use for deposits
     * @param amounth of for deposit
     */
    void deposit(AccountId accountId, BigDecimal amounth) throws NullParameterException, AccountServiceException, AccountException;

    /**
     *
     * @param accountId of Account which would use for withdraw
     * @param amounth of for amounth
     */
    void withDraw(AccountId accountId, BigDecimal amounth) throws AccountServiceException, NullParameterException, AccountException, TransactionException;

    /**
     * money transfer btw two accounts
     * @param senderAcountId sender acount id
     * @param receiverAccountId receiver acount id
     * @param amounth of the money which would be transfered.
     */
    void transfer(AccountId senderAcountId,AccountId receiverAccountId, BigDecimal amounth) throws NullParameterException, AccountServiceException, AccountException, TransactionException;

}
