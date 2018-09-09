package pl.org.revolut.model;

import org.junit.jupiter.api.Test;
import pl.org.revolut.exception.PhoneNumberException;
import pl.org.revolut.model.identifier.AccountId;
import pl.org.revolut.model.identifier.CustomerId;
import pl.org.revolut.model.identifier.TransactionId;
import pl.pojo.tester.api.assertion.Method;
import pl.pojo.tester.internal.field.AbstractFieldValueChanger;
import pl.pojo.tester.internal.field.DefaultFieldValueChanger;

import java.time.Instant;
import java.util.Date;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class TransactionTest {
    @Test
    public void testTransaction() {
        assertPojoMethodsFor(Transaction.class).testing(Method.values()).areWellImplemented();
    }

}
