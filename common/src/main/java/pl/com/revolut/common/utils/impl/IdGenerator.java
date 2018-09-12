package pl.com.revolut.common.utils.impl;

public class IdGenerator {

    private static long accountIdCount = 1;
    private static long customerIdCount = 1;
    private static long transactionIdCount = 1;
    public static final String CUSTOMER_ID_PREFIX = "CUS";
    public static final String TRANSACTION_ID_PREFIX = "TRA";
    public static final String ACCOUNT_ID_PREFIX = "ACC";
    public static final String DASH = "-";

    private IdGenerator(){}

    public static String generateAccountId()
    {
        return new StringBuilder().append(ACCOUNT_ID_PREFIX).append(DASH).append(String.valueOf(accountIdCount++)).toString();
    }


    public static String generateCustomerId()
    {
        return new StringBuilder().append(CUSTOMER_ID_PREFIX).append(DASH).append(String.valueOf(customerIdCount++)).toString();
    }


    public static String generateTransactionId()
    {
        return new StringBuilder().append(TRANSACTION_ID_PREFIX).append(DASH).append(String.valueOf(transactionIdCount++)).toString();
    }

}
