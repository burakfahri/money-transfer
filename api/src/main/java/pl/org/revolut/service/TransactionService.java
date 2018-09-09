package pl.org.revolut.service;

import pl.org.revolut.model.Transaction;
import pl.org.revolut.model.identifier.TransactionId;

import java.util.List;

public interface TransactionService {

    /**
     *
     * @return all the transactions
     */

    List<Transaction> getAllTransactions();

    /**
     * add or update the transaction
     */
    void addOrUpdateTransaction(Transaction transaction);


    /**
     * remove existing transaction by transactionId
     * return true if it can delete
     */
    Boolean removeTransaction(TransactionId transactionId);

    /**
     * @param transactionId belongs to transaction
     * gettransactionById
     * @return transaction by {@code transactionId}
     */
    Transaction getTransactionById(TransactionId transactionId);

}
