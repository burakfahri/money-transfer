package pl.com.revolut.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.CustomerId;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Bank account of the Customer
 */
public class Account {
    private AccountId accountId; //Id of the account ,starts with ACC-{id}
    private Date openDate;//open date of the account
    private BigDecimal currentBalance; //current balance of the account
    @NonNull
    private CustomerId customerId;//customer id belongs the owner of the account

}
