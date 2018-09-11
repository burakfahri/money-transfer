package pl.com.revolut.exception;

public class ZipCodeException extends  Exception{
    private static final String WRONG_ZIP_CODE = "ZIP CODE IS WRONG";

    public ZipCodeException(){
        super(WRONG_ZIP_CODE);
    }

    public ZipCodeException(final String message)
    {
        super(message);
    }
}
