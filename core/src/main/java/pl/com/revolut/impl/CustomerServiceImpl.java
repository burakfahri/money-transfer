package pl.com.revolut.impl;

import pl.com.revolut.common.service.StorageService;
import pl.com.revolut.common.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.ServiceImplUtils;
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.identifier.CustomerId;
import pl.com.revolut.service.CustomerService;

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
        ServiceImplUtils.checkParameters(customer);
        super.addOrUpdateItem(customer.getCustomerId(),customer);
    }

    @Override
    public Boolean removeCustomer(CustomerId customerId) throws NullParameterException {
        ServiceImplUtils.checkParameters(customerId);

        return super.remove(customerId) != null;
    }

    @Override
    public Customer getCustomerById(CustomerId customerId) throws NullParameterException {
        ServiceImplUtils.checkParameters(customerId);
        return super.getItem(customerId);
    }
}
