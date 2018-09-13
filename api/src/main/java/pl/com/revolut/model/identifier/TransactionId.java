package pl.com.revolut.model.identifier;

import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;

/**
 * Id of an transaction
 */
public class TransactionId extends Id {
    private transient  String TRANSACTION_ID_PREFIX = "TRA";

    /**
     * constructor of the TransactionId
     * @param id of the transaction
     * @throws NullParameterException when the {@param id is null}
     *
     * @throws IdException when the id is not valid
     */
    public TransactionId(String id) throws NullParameterException, IdException {
        super(id);
        if(!validateTransactionId(id)) throw new IdException();
    }

    /**
     * validate the transaction id is correct or not
     * @param transactionId
     * @return the validation result of transaction id
     * @throws NullParameterException when the {@param transactionId} is null
     */
    private  Boolean validateTransactionId(String transactionId) throws NullParameterException {
        if(transactionId == null)
            throw new  NullParameterException();

        String[] splittedId = transactionId.split(DASH);
        return  (splittedId.length == 2) && splittedId[0].equals(TRANSACTION_ID_PREFIX) && splittedId[1].chars().allMatch(Character::isDigit);
    }
}
