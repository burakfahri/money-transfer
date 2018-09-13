package pl.com.revolut.model.identifier;


import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;

/**
 * Id of an account
 */
public class CustomerId extends Id {
    private transient final String CUSTOMER_ID_PREFIX = "CUS";//it starts with CUS-
    /**
     * constructor of the CustomerId
     * @param id of the account
     * @throws NullParameterException when the {@param id is null}
     *
     * @throws IdException when the id is not valid
     */
    public CustomerId(String id) throws IdException, NullParameterException {
        super(id);
        if(!validateCustomerId(id)) throw new IdException();
    }


    /**
     * validate the customer id is correct or not
     * @param customerId
     * @return the validation result of customer id
     * @throws NullParameterException when the {@param customerId} is null
     */
    private  Boolean validateCustomerId(String customerId) throws NullParameterException {
        if(customerId == null)
            throw new  NullParameterException();

        String[] splittedId = customerId.split(DASH);
        return  (splittedId.length == 2) && splittedId[0].equals(CUSTOMER_ID_PREFIX) && splittedId[1].chars().allMatch(Character::isDigit);
    }
}
