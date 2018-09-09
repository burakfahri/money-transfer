package pl.org.revolut.exception;

public class ZipCodeException extends  Exception{
    public static final String WRONG_ZIP_CODE = "ZIP CODE IS WRONG";

    public ZipCodeException(){
        super(WRONG_ZIP_CODE);
    }

    public ZipCodeException(final String message)
    {
        super(message);
    }
}
