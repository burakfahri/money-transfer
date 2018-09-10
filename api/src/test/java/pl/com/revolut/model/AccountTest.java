package pl.com.revolut.model;

import org.junit.jupiter.api.Test;
import pl.pojo.tester.api.assertion.Method;


import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class AccountTest {
    @Test
    public void testAccount() {
                assertPojoMethodsFor(Account.class).testing(Method.values()).areWellImplemented();
    }
}
