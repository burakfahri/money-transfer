package pl.com.revolut.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.com.revolut.model.identifier.CustomerId;


import java.util.Date;

/**
 * used lombok to generate data model objects
 *
 * @See <a href="https://projectlombok.org/features/all">lombok annotations</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * customer is a person who can open a accounts and make transactions
 */
public class Customer {
    private CustomerId customerId;//id of the customer
    private String customerName;//name of the customer
    private String customerSurname;//surname of the customer
    private PhoneNumber customerPhone;//phone number of the customer
    private Date attendDate;//attend date of the customer to the system
}
