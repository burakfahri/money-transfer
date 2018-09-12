package pl.com.revolut.model;

import org.junit.Assert;
import org.junit.Test;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.CustomerId;
import pl.pojo.tester.api.assertion.Method;

import java.math.BigDecimal;
import java.util.Date;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class AccountTest {
    @Test
    public void testAccountGetterAndToString() {
        assertPojoMethodsFor(Account.class).testing(Method.GETTER,Method.TO_STRING).areWellImplemented();
    }

    @Test
    public void testAccountConstructorAndSetters() throws NullParameterException, IdException {
         Date date = new Date();
        Account account = new Account(new AccountId("ACC-1"),date,new BigDecimal(100.0),new CustomerId("CUS-1"));
        Account account1 = new Account(new AccountId("ACC-1"),date,new BigDecimal(100.0),new CustomerId("CUS-1"));
        Account account2 = new Account();
        Account account3 = new Account();
        account3.setAccountId(new AccountId("ACC-1"));
        account3.setOpenDate(date);
        account3.setCurrentBalance(new BigDecimal(100.0));
        account3.setCustomerId(new CustomerId("CUS-1"));
        Assert.assertTrue(account.equals(account1));
        Assert.assertEquals(account,account3);
        Assert.assertNotEquals(account,account2);
    }

    @Test(expected = IdException.class)
    public void testCustomerExceptionConstructor() throws NullParameterException, IdException {
        new Account(new AccountId("1"),new Date(),new BigDecimal(100.0),new CustomerId("CUS-1"));

    }


}
