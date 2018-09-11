package pl.com.revolut.model.identifier;

import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;


public class TransactionId extends Id {
    private String TRANSACTION_ID_PREFIX = "TRA";
    private String DASH = "-";
    public TransactionId(String id) throws NullParameterException, IdException {
        super(id);
        if(!validateTransactionId(id)) throw new IdException();

    }
    private  Boolean validateTransactionId(String transactionId) throws NullParameterException {
        if(transactionId == null)
            throw new  NullParameterException();

        String[] splittedId = transactionId.split(DASH);
        return  (splittedId.length == 2) && splittedId[0].equals(TRANSACTION_ID_PREFIX) && splittedId[1].chars().allMatch(Character::isDigit);
    }
}
