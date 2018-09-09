package pl.org.revolut.impl;

import pl.org.revolut.app.impl.StorageService;
import pl.org.revolut.exception.NullParameterException;
import pl.org.revolut.model.Customer;
import pl.org.revolut.model.identifier.CustomerId;
import pl.org.revolut.service.CustomerService;

import java.util.List;


public class CustomerServiceImpl extends StorageService<CustomerId,Customer> implements CustomerService  {
    private static CustomerService customerServiceInstance = null;

    private CustomerServiceImpl() {
        super();
    }

    public synchronized static CustomerService getCustomerServiceInstance()
    {
        if(customerServiceInstance == null)
            customerServiceInstance = new CustomerServiceImpl();
        return customerServiceInstance;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return super.getAll();
    }

    @Override
    public void addOrUpdateCustomer(Customer customer) throws NullParameterException {
        if(customer == null)
            throw new NullParameterException("CUSTOMER IS NULL");
        super.addOrUpdateItem(customer.getCustomerId(),customer);
    }

    @Override
    public Boolean removeCustomer(CustomerId customerId) throws NullParameterException {
        if(customerId == null)
            throw new NullParameterException("CUSTOMER ID IS NULL");
        return super.remove(customerId) != null;
    }

    @Override
    public Customer getCustomerById(CustomerId customerId) throws NullParameterException {
        if(customerId == null)
            throw new NullParameterException("CUSTOMER ID IS NULL");
        return super.getItem(customerId);
    }
}
