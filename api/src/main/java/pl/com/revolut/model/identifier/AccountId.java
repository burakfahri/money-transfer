package pl.com.revolut.model.identifier;

import pl.com.revolut.common.exception.IdException;
import pl.com.revolut.common.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.IdGenerator;

public class AccountId extends Id {

    public AccountId(String id) throws NullParameterException, IdException {
        super(id);
        if(!IdGenerator.validateAccountId(id)) throw new IdException();

    }
}
