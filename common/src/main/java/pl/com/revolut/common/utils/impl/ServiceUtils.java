package pl.com.revolut.common.utils.impl;

import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.model.Transaction;
import pl.com.revolut.model.TransactionType;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.TransactionId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public final class ServiceUtils {
    private ServiceUtils(){}
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ServiceUtils.class);

    /**
     * check the parametes are null
     * @param paramters
     * @throws NullParameterException if the one of the parameter is null
     */
    public static void  checkParameters(Object ...paramters) throws NullParameterException {
        for (Object parameter : paramters)
            if(parameter == null)
            {
                logger.error("Parameter is null");
                throw new NullParameterException(new StringBuilder().append("PARAMETER IS NULL").toString());
            }
    }

    /**
     * create transaction according to its {@param transactionType}
     * @param amount of the transaction
     * @param transactionType of the transaction , the method would use for deposit and withdraw transactions
     * @param accountId of the account
     * @return the transaction which is created from the operation
     * @throws NullParameterException if the parameters are null
     * @throws IdException {@param accountId} is not valid
     */
    public static Transaction createDepositOrWithDrawTransaction(BigDecimal amount, TransactionType transactionType,
                                                                 AccountId accountId)
            throws NullParameterException, IdException {
        checkParameters(accountId,transactionType,amount);
        Transaction transaction = new Transaction();
        switch (transactionType)
        {
            case DEPOSIT:
                transaction.setSenderAccountId(accountId);
                transaction.setTransactionType(TransactionType.DEPOSIT);
                break;
            case WITHDRAW:
                transaction.setTransactionType(TransactionType.WITHDRAW);
                transaction.setReceiverAccountId(accountId);
                break;
            default:
                return null;
        }
        transaction.setTransactionId(new TransactionId(IdGenerator.generateTransactionId()));
        transaction.setAmount(amount);
        transaction.setDate(Calendar.getInstance().getTime());
        return transaction;
    }

    /**
     * create transaction according to its {@param transactionType}
     * @param amount of the transaction from one account to another
     * @param senderAccountId which belongs to the account of the sender
     * @param receiverAccountId which belongs to the account of the receiver
     * @param explanation short description of the transaction (optional)
     * @return the transaction which type is TRANSFER
     * @throws NullParameterException if the parameters are null
     * @throws IdException {@param accountId} is not valid
     */
    public static Transaction createTransferTransaction(BigDecimal amount, AccountId senderAccountId,
                                                           AccountId receiverAccountId,
                                                           String explanation)
            throws NullParameterException, IdException {
        checkParameters(senderAccountId,receiverAccountId,amount);
        Transaction transaction = new Transaction();

        transaction.setTransactionId(new TransactionId(IdGenerator.generateTransactionId()));
        transaction.setAmount(amount);

        if(explanation != null)
            transaction.setExplanation(explanation);
        transaction.setReceiverAccountId(receiverAccountId);
        transaction.setSenderAccountId(senderAccountId);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setDate(Calendar.getInstance().getTime());
        return transaction;
    }
}
