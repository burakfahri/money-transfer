package pl.com.revolut.model;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@ToString
public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAW("WithDraw"),
    TRANSFER("Transfer");

    @Setter
    @Getter
    private String type;
}
