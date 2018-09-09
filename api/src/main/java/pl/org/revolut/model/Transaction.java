package pl.org.revolut.model;

import lombok.Data;
import pl.org.revolut.model.identifier.AccountId;
import pl.org.revolut.model.identifier.TransactionId;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Transaction {
    TransactionId transactionId;
    AccountId senderAccountId;
    AccountId receiverAccountId;
    Date date;
    BigDecimal amount;
    String explanation;
}
