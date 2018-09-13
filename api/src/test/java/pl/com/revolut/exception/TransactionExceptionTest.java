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
public class TransactionExceptionTest {


	@Test(expected = TransactionException.class)
	public void testConstructor() throws TransactionException {
		TransactionException exception = new TransactionException("Dummy");
		Assert.assertNotNull("TransactionException can not be null", exception);
		throw exception;
	}

	@Test
	public void testConstructorAuto() {
		Assertions.assertPojoMethodsFor(TransactionException.class).testing(Method.CONSTRUCTOR).areWellImplemented();
	}
}
