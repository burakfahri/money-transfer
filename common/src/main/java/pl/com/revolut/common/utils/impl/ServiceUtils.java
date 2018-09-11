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

    public static void  checkParameters(Object ...paramters) throws NullParameterException {
        for (Object parameter : paramters)
            if(parameter == null)
            {
                logger.error("Parameter is null");
                throw new NullParameterException(new StringBuilder().append("PARAMETER IS NULL").toString());
            }
    }

    public static Transaction createDepositOrWithDrawTransaction(BigDecimal amount, TransactionType transactionType,
                                                                 AccountId accountId)
            throws NullParameterException, IdException {
        checkParameters(accountId,transactionType,amount);
        Transaction transaction = new Transaction();
        switch (transactionType)
        {
            case DEPOSIT:
                transaction.setSenderAccountId(accountId);
                break;
            case WITHDRAW:
                transaction.setReceiverAccountId(accountId);
                break;
        }
        transaction.setTransactionId(new TransactionId(IdGenerator.generateTransactionId()));
        transaction.setAmount(amount);
        return transaction;
    }

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
