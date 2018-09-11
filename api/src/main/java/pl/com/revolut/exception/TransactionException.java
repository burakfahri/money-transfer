package pl.com.revolut.exception;

public class TransactionException extends Exception{
    private static final String TRANSACTION_EXCEPTION = "THERE IS AN ERROR IN TRANSACTION";

    public TransactionException(){
        super(TRANSACTION_EXCEPTION);
    }

    public TransactionException(String message)
    {
        super(message);
    }
}
