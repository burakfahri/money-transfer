/**
 * 
 */
package pl.com.revolut.exception;

import org.junit.Assert;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;

/**
 * @author resulav
 *
 */
public class AccountExceptionTest {


	@Test(expected = AccountException.class)
	public void testConstructor() throws AccountException {
		AccountException exception = new AccountException("Dummy");
		Assert.assertNotNull("AccountException can not be null", exception);
		throw exception;
	}

	@Test
	public void testConstructorAuto() {
		Assertions.assertPojoMethodsFor(AccountException.class).testing(Method.CONSTRUCTOR).areWellImplemented();
	}
}
