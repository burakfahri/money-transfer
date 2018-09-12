package pl.com.revolut.model;

import org.junit.Assert;
import org.junit.Test;
import pl.com.revolut.exception.PhoneNumberException;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class PhoneNumberTest {
    @Test
    public void testGetterAndToString(){
        assertPojoMethodsFor(PhoneNumber.class).testing(Method.GETTER,Method.TO_STRING).areWellImplemented();
    }

    @org.junit.Test
    public void testAccountConstructorAndSetters() throws PhoneNumberException{



        PhoneNumber phoneNumber = new PhoneNumber(210,210,210);
        PhoneNumber phoneNumber1 = new PhoneNumber(210,210,210);
        PhoneNumber phoneNumber2 = new PhoneNumber();
        PhoneNumber phoneNumber3 = new PhoneNumber();
        phoneNumber3.setArea(210);
        phoneNumber3.setExch(210);
        phoneNumber3.setExt(210);
        Assert.assertTrue(phoneNumber.equals(phoneNumber1));
        Assert.assertEquals(phoneNumber,phoneNumber3);
        Assert.assertNotEquals(phoneNumber,phoneNumber2);
    }


    @org.junit.Test(expected=PhoneNumberException.class)
    public void testPhoneNumberException() throws PhoneNumberException {
        PhoneNumber phoneNumber1 = new PhoneNumber(100,210,210);
    }
}
