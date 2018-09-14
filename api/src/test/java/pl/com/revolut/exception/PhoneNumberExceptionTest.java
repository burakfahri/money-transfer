/**
 * 
 */
package pl.com.revolut.exception;

import org.junit.Assert;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;

public class PhoneNumberExceptionTest {


	@Test(expected = PhoneNumberException.class)
	public void testConstructor() throws PhoneNumberException {
		PhoneNumberException exception = new PhoneNumberException("Dummy");
		Assert.assertNotNull("PhoneNumberException can not be null", exception);
		throw exception;
	}

	@Test
	public void testConstructorAuto() {
		Assertions.assertPojoMethodsFor(PhoneNumberException.class).testing(Method.CONSTRUCTOR).areWellImplemented();
	}
}
