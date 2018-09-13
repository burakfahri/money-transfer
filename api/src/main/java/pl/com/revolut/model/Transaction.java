package pl.com.revolut.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.revolut.model.identifier.TransactionId;
import pl.com.revolut.model.identifier.AccountId;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * an instance of exchanging money
 */
public class Transaction {
    TransactionId transactionId; //identifier of a transaction , it starts with TRA-{id}
    AccountId senderAccountId;//account id of sender side , it is null when withdraw transactions
    AccountId receiverAccountId;////account id of receiver side , it is null when deposit transactions
    Date date;//date of the transaction occur
    BigDecimal amount;//amont of the transaction
    String explanation;//special note of the transaction
    TransactionType transactionType;//type of the transaction, it can be DEPOSIT, WITHDRAW OR TRANSFER
}
