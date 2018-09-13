package pl.com.revolut.common.utils.impl;

/**
 * Id generator of the
 * {@link pl.com.revolut.model.identifier.AccountId}
 * {@link pl.com.revolut.model.identifier.CustomerId}
 * {@link pl.com.revolut.model.identifier.TransactionId}
 */
public class IdGenerator {

    private static long accountIdCount = 1;
    private static long customerIdCount = 1;
    private static long transactionIdCount = 1;
    public static final String CUSTOMER_ID_PREFIX = "CUS";
    public static final String TRANSACTION_ID_PREFIX = "TRA";
    public static final String ACCOUNT_ID_PREFIX = "ACC";
    public static final String DASH = "-";

    private IdGenerator(){}

    /**
     * generates the account id like ACC-{accountIdCount}
     * @return the generated id
     */
    public static String generateAccountId()
    {
        return new StringBuilder().append(ACCOUNT_ID_PREFIX).append(DASH).append(String.valueOf(accountIdCount++)).toString();
    }


    /**
     * generates the customer id like CUS-{customerIdCount}
     * @return the generated id
     */
    public static String generateCustomerId()
    {
        return new StringBuilder().append(CUSTOMER_ID_PREFIX).append(DASH).append(String.valueOf(customerIdCount++)).toString();
    }


    /**
     * generates the transaction id like TRA-{transactionIdCount}
     * @return the generated id
     */
    public static String generateTransactionId()
    {
        return new StringBuilder().append(TRANSACTION_ID_PREFIX).append(DASH).append(String.valueOf(transactionIdCount++)).toString();
    }

}
