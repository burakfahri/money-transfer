package pl.com.revolut.common.exception;

public class IdException extends Exception{
    private static final String INVALID_ID = "ID IS NOT VALID";

    public IdException(){
        super(INVALID_ID);
    }

    public IdException(String message)
    {
        super(message);
    }
}
