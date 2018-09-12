package pl.com.revolut.model;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@ToString
public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAW("WithDraw"),
    TRANSFER("Transfer");


    private String type;
}
