package pl.com.revolut.model;

import lombok.Data;
import pl.com.revolut.model.identifier.TransactionId;
import pl.com.revolut.model.identifier.AccountId;

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
    TransactionType transactionType;
}
