package pl.org.revolut.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.org.revolut.model.identifier.AccountId;
import pl.org.revolut.model.identifier.CustomerId;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class Account {
    @NonNull
    AccountId accountId;
    Date openDate;
    Date closeDate;
    BigDecimal currentBalance;
    @NonNull
    CustomerId customerId;
}
