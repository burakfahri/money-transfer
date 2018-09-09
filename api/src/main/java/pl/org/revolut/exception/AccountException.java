package pl.org.revolut.exception;

public class AccountException extends Exception{
    private static final String NULL_ACCOUNT = "ACCOUNT IS NULL";

    public AccountException(){
        super(NULL_ACCOUNT);
    }

    public AccountException(String message)
    {
        super(message);
    }
}
