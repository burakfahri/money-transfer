package pl.com.revolut.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.com.revolut.model.identifier.CustomerId;


import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    CustomerId customerId;
    String customerName;
    String customerSurname;
    PhoneNumber customerPhone;
    Date attendDate;
}
