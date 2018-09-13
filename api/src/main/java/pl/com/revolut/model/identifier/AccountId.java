package pl.com.revolut.model.identifier;

import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;

/**
 * Id of an account
 */
public class AccountId extends Id {
    private transient final String ACCOUNT_ID_PREFIX = "ACC";//it starts with ACC-

    /**
     * constructor of the AccountId
     * @param id of the account
     * @throws NullParameterException when the {@param id is null}
     * @throws IdException when the id is not valid
     */
    public AccountId(String id) throws NullParameterException, IdException {
        super(id);
        if(!validateAccountId(id)) throw new IdException();

    }

    /**
     * validate the account id is correct or not
     * @param accountId
     * @return the validation result of account id
     * @throws NullParameterException when the {@param: accountId} is null
     */
    private  Boolean validateAccountId(String accountId) throws NullParameterException {
        if(accountId == null)
            throw new  NullParameterException();

        String[] splittedId = accountId.split(DASH);
        return  (splittedId.length == 2) && splittedId[0].equals(ACCOUNT_ID_PREFIX) && splittedId[1].chars().allMatch(Character::isDigit);
    }

}
