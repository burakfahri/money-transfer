package pl.org.revolut.exception;

public class PhoneNumberException extends Exception{
    public static final String WRONG_PHONE_NUMBER = "PHONE NUMBER IS WRONG";

    public PhoneNumberException(){
        super(WRONG_PHONE_NUMBER);
    }

    public PhoneNumberException(String message)
    {
        super(message);
    }
}
