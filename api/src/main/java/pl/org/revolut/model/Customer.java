package pl.org.revolut.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.org.revolut.model.identifier.CustomerId;


import java.util.Date;

@Data
@NoArgsConstructor
public class Customer {
    @NonNull
    CustomerId customerId;
    String customerName;
    String customerSurname;
    PhoneNumber customerPhone;
    Date attendDate;
    Date lastOperationDate;
}
