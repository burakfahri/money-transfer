/**
 * 
 */
package pl.com.revolut.exception;

import org.junit.Assert;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;


public class IdExceptionTest {


	@Test(expected = IdException.class)
	public void testConstructor() throws IdException {
		IdException exception = new IdException("Dummy");
		Assert.assertNotNull("IdException can not be null", exception);
		throw exception;
	}

	@Test
	public void testConstructorAuto() {
		Assertions.assertPojoMethodsFor(IdException.class).testing(Method.CONSTRUCTOR).areWellImplemented();
	}
}
