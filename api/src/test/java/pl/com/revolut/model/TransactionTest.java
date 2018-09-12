package pl.com.revolut.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.CustomerId;
import pl.com.revolut.model.identifier.TransactionId;
import pl.pojo.tester.api.assertion.Method;

import java.math.BigDecimal;
import java.util.Date;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class TransactionTest {
    @Test
    public void testTransaction() {
        assertPojoMethodsFor(Transaction.class).testing(Method.GETTER,Method.TO_STRING).areWellImplemented();
    }

    @org.junit.Test
    public void testAccountConstructorAndSetters() throws pl.com.revolut.exception.NullParameterException, IdException {
        Date date = new Date();

        Transaction transaction =  new Transaction(new TransactionId("TRA-1"),new AccountId("ACC-1"),
                new AccountId("ACC-2"), date,new BigDecimal(100.0),"EXP1",TransactionType.TRANSFER);

        Transaction transaction1 = new Transaction(new TransactionId("TRA-1"),new AccountId("ACC-1"),
                new AccountId("ACC-2"),date,new BigDecimal(100.0),"EXP1",TransactionType.TRANSFER);

        Transaction transaction2 = new Transaction();
        Transaction transaction3 = new Transaction();

        transaction3.setTransactionId(new TransactionId("TRA-1"));
        transaction3.setSenderAccountId(new AccountId("ACC-1"));
        transaction3.setReceiverAccountId(new AccountId("ACC-2"));
        transaction3.setTransactionType(TransactionType.TRANSFER);
        transaction3.setAmount(new BigDecimal(100.0));
        transaction3.setDate(date);
        transaction3.setExplanation("EXP1");
        Assert.assertTrue(transaction.equals(transaction1));
        Assert.assertEquals(transaction,transaction3);
        Assert.assertNotEquals(transaction,transaction2);
    }


    @org.junit.Test(expected=IdException.class)
    public void testTransactionIDException() throws pl.com.revolut.exception.NullParameterException, IdException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(new TransactionId("1"));
    }
}
