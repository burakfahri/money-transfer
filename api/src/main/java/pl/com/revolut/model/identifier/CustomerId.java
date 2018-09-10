package pl.com.revolut.model.identifier;


import pl.com.revolut.common.exception.IdException;
import pl.com.revolut.common.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.IdGenerator;

public class CustomerId extends Id {
    public CustomerId(String id) throws IdException, NullParameterException {
        super(id);
        if(!IdGenerator.validateCustomerId(id)) throw new IdException();
    }
}
