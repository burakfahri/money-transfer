package pl.com.revolut.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.com.revolut.model.identifier.CustomerId;


import java.util.Date;

@Data
@NoArgsConstructor
public class Customer {
    CustomerId customerId;
    String customerName;
    String customerSurname;
    PhoneNumber customerPhone;
    Date attendDate;
    Date lastOperationDate;
}
