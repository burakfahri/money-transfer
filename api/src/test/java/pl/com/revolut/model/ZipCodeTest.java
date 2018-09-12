package pl.com.revolut.model;

import org.junit.Assert;
import org.junit.Test;
import pl.com.revolut.exception.ZipCodeException;


public class ZipCodeTest{

	private final String []zipCodes = {"06400", "06500","04559","abc"};

	@Test
	public void testEqualsZipCode() {
		ZipCode zipCode = null;
		for (int i = 0; i< 3 ; i++)
			try {
				zipCode = new ZipCode(zipCodes[i]);
				Assert.assertEquals(zipCodes[i], zipCode.getZipCode());
			} catch (ZipCodeException e) {
				e.printStackTrace();
			}
	}

	@Test
	public void testHashCode(){
		ZipCode zipCode = null;
		ZipCode zipCode1 = null;
		try {
			zipCode = new ZipCode();
			zipCode.setZipCode(zipCodes[0]);
			zipCode1 = new ZipCode(zipCodes[1]);
		} catch (ZipCodeException e) {
			e.printStackTrace();
		}
		Assert.assertNotEquals(zipCode.hashCode(),zipCode1.hashCode());
	}


	@Test(expected = ZipCodeException.class)
	public void testExceptionEquals() throws ZipCodeException{
		ZipCode zipCode = new ZipCode(zipCodes[3]);
	}
}
