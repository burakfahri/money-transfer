package pl.com.revolut.web;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.revolut.exception.AccountException;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.identifier.CustomerId;
import pl.com.revolut.service.CustomerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.URI;

@RequestMapping("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Controller
@Slf4j
public class CustomerWebService {
    private Gson gson = new Gson();

    private CustomerService customerService;

    @Autowired
    public CustomerWebService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity getAllCustomers(){
        String customerListJson = gson.toJson(customerService.getAllCustomers());
        log.debug(customerListJson);
        return ResponseEntity.ok(customerListJson);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity getCustomerByCustomerId(@PathVariable("customerId") String customerId){
        Customer customer;
        try {
            customer = customerService.getCustomerById(new CustomerId(customerId));
            if(customer == null)
                return ResponseEntity.notFound().build();
        }  catch (NullParameterException | IdException | JsonSyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(gson.toJson(customer));
    }


    @PostMapping
    public ResponseEntity createCustomer(@RequestBody String stringCustomer){
        URI uri ;
        try {
            Customer customer = gson.fromJson(stringCustomer,Customer.class);
            if(customer.getCustomerId() != null)
                return ResponseEntity.notFound().build();

            String customerId = IdGenerator.generateCustomerId();
            customer.setCustomerId(new CustomerId(customerId));

            uri = ServletUriComponentsBuilder.fromCurrentRequest().path(
                    "/{id}").buildAndExpand(customer.getCustomerId()).toUri();

            customerService.addOrUpdateCustomer(customer);
        } catch (NullParameterException | IdException | JsonSyntaxException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{customerId}")
    public ResponseEntity updateCustomer(@RequestBody String stringCustomer,@PathVariable("customerId") String customerId){

        Customer customer;

        try {
            customer = gson.fromJson(stringCustomer,Customer.class);

            Customer oldCustomer = customerService.getCustomerById(new CustomerId(customerId));

            if(oldCustomer == null)
                return ResponseEntity.notFound().build();

            customer.setCustomerId(new CustomerId(customerId));
            
            customerService.addOrUpdateCustomer(customer);

        } catch (NullParameterException | IdException | JsonSyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(gson.toJson(customer));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity deleteCustomer(@PathVariable("customerId") String customerId){
        Customer customer;
        try {
            customer = customerService.removeCustomer(new CustomerId(customerId));
            if(customer == null)
                return ResponseEntity.notFound().build();
        }catch (NullParameterException | IdException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AccountException | JsonSyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(gson.toJson(customer));
    }

}
