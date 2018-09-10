package pl.com.revolut.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.CustomerId;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class Account {
    AccountId accountId;
    Date openDate;
    Date closeDate;
    BigDecimal currentBalance;
    @NonNull
    CustomerId customerId;
}
