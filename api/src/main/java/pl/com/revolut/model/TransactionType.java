package pl.com.revolut.model;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@ToString
/**
 * type of a transaction
 */
public enum TransactionType {
    DEPOSIT("Deposit"), //transfer money to customer's own account
    WITHDRAW("WithDraw"),//transfer money from customer's own account
    TRANSFER("Transfer");//transfer money from an account from another

    private String type;
}
