package pl.com.revolut.model;

import org.junit.jupiter.api.Test;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class TransactionTest {
    @Test
    public void testTransaction() {
        assertPojoMethodsFor(Transaction.class).testing(Method.values()).areWellImplemented();
    }

}
