package pl.org.revolut.exception;

public class AccountServiceException extends Exception{
    private static final String ACCOUNT_SERVICE_NULL = "ACCOUNT SERVICE IS NULL";

    public AccountServiceException(){
        super(ACCOUNT_SERVICE_NULL);
    }

    public AccountServiceException(String message)
    {
        super(message);
    }
}
