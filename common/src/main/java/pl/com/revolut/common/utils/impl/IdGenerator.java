package pl.com.revolut.common.utils.impl;

import pl.com.revolut.common.exception.NullParameterException;

public class IdGenerator {

    private static long accountIdCount = 1;
    private static long customerIdCount = 1;
    public static final String ACCOUNT_ID_PREFIX = "ACC";
    public static final String CUSTOMER_ID_PREFIX = "CUS";
    public static final String DASH = "-";

    private IdGenerator(){}

    public static String generateAccountId()
    {
        return new StringBuilder().append(ACCOUNT_ID_PREFIX).append(DASH).append(String.valueOf(accountIdCount++)).toString();
    }


    public static String generateCustomerId()
    {
        return new StringBuilder().append(CUSTOMER_ID_PREFIX).append(String.valueOf(customerIdCount++)).toString();
    }

    public static Boolean validateAccountId(String accountId) throws NullParameterException {
        ServiceImplUtils.checkParameters(accountId);
        String[] splittedId = accountId.split(DASH);
        return  (splittedId.length == 2) && splittedId[0].equals(ACCOUNT_ID_PREFIX) && splittedId[1].chars().allMatch(Character::isDigit)
        && Integer.parseInt(splittedId[1]) < accountIdCount;
    }

    public static Boolean validateCustomerId(String customerId) throws NullParameterException {
        ServiceImplUtils.checkParameters(customerId);
        String[] splittedId = customerId.split(DASH);
        return  (splittedId.length == 2) && splittedId[0].equals(CUSTOMER_ID_PREFIX) && splittedId[1].chars().allMatch(Character::isDigit)
                && Integer.parseInt(splittedId[1]) < customerIdCount;
    }
}
