package pl.com.revolut.model.identifier;

import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;

public class AccountId extends Id {
    private final String ACCOUNT_ID_PREFIX = "ACC";
    private final String DASH = "-";

    public AccountId(String id) throws NullParameterException, IdException {
        super(id);
        if(!validateAccountId(id)) throw new IdException();

    }

    private  Boolean validateAccountId(String accountId) throws NullParameterException {
        if(accountId == null)
            throw new  NullParameterException();

        String[] splittedId = accountId.split(DASH);
        return  (splittedId.length == 2) && splittedId[0].equals(ACCOUNT_ID_PREFIX) && splittedId[1].chars().allMatch(Character::isDigit);
    }
}
