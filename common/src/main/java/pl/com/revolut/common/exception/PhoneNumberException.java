package pl.com.revolut.common.exception;

public class PhoneNumberException extends Exception{
    private static final String WRONG_PHONE_NUMBER = "PHONE NUMBER IS WRONG";

    public PhoneNumberException(){
        super(WRONG_PHONE_NUMBER);
    }

    public PhoneNumberException(String message)
    {
        super(message);
    }
}
