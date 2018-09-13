package pl.com.revolut.model.identifier;


import lombok.EqualsAndHashCode;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;

public class CustomerId extends Id {
    private transient final String CUSTOMER_ID_PREFIX = "CUS";

    public CustomerId(String id) throws IdException, NullParameterException {
        super(id);
        if(!validateCustomerId(id)) throw new IdException();
    }

    private  Boolean validateCustomerId(String accountId) throws NullParameterException {
        if(accountId == null)
            throw new  NullParameterException();

        String[] splittedId = accountId.split(DASH);
        return  (splittedId.length == 2) && splittedId[0].equals(CUSTOMER_ID_PREFIX) && splittedId[1].chars().allMatch(Character::isDigit);
    }
}
