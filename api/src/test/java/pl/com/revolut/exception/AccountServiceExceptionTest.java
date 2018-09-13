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
public class AccountServiceExceptionTest {


	@Test(expected = AccountServiceException.class)
	public void testConstructor() throws AccountServiceException {
		AccountServiceException exception = new AccountServiceException("Dummy");
		Assert.assertNotNull("AccountServiceException can not be null", exception);
		throw exception;
	}

	@Test
	public void testConstructorAuto() {
		Assertions.assertPojoMethodsFor(AccountServiceException.class).testing(Method.CONSTRUCTOR).areWellImplemented();
	}
}
