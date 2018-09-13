package pl.com.revolut.web;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.Logger;
import pl.com.revolut.common.utils.impl.ServiceUtils;
import pl.com.revolut.exception.AccountException;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.common.utils.web.WebUtils;
import pl.com.revolut.exception.PhoneNumberException;
import pl.com.revolut.impl.CustomerServiceImpl;
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.PhoneNumber;
import pl.com.revolut.model.identifier.CustomerId;
import pl.com.revolut.service.CustomerService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;

@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerWebService {
    private final CustomerService customerService = CustomerServiceImpl.getCustomerServiceInstance();
    private static final Logger log = Logger.getLogger(AccountWebService.class);
    private Gson gson = new Gson();


    @GET
    public Response getAllAccounts(){
        String customerListJson = gson.toJson(customerService.getAllCustomers());
        log.debug(customerListJson);
        return Response.ok(customerListJson).build();
    }

    @GET
    @Path("/{customerId}")
    public Response getCustomerByCustomerId(@PathParam("customerId") String customerId){
        Customer customer = null;
        try {
            customer = customerService.getCustomerById(new CustomerId(customerId));
            if(customer == null)
                return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NullParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Customer id con not be null").build();
        }catch (IdException ie) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Customer id is not valid").build();
        }catch (JsonSyntaxException je) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Json is not valid").build();
        }
        return Response.ok(gson.toJson(customer)).build();
    }

    @POST
    @Path("/createFakeCustomers")
    public Response createFakeCustomers(@Context UriInfo uriInfo) {
        try {
            addFakeCustomerData();
        } catch (PhoneNumberException e) {
            e.printStackTrace();
        } catch (NullParameterException e) {
            e.printStackTrace();
        } catch (IdException e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }


    @POST
    public Response createCustomer(String stringCustomer,@Context UriInfo uriInfo){

        Customer customer = null;
        URI uri = null;
        try {
            log.info(uriInfo);
            customer = gson.fromJson(stringCustomer,Customer.class);
            if(customer.getCustomerId() != null)
                return Response.status(Response.Status.BAD_REQUEST).entity("Customer id must " +
                        "be null while creating new customer").build();

            String customerId = IdGenerator.generateCustomerId();
            customer.setCustomerId(new CustomerId(customerId));

            uri = WebUtils.generateUri(uriInfo,customerId);
            customerService.addOrUpdateCustomer(customer);
        }catch (NullParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Customer model is wrong").build();
        }catch (IdException ie) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Customer id is not valid").build();
        }catch (JsonSyntaxException je) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Json is not valid").build();
        }

        return Response.created(uri).build();
    }

    @PUT
    @Path("/{customerId}")
    public Response updateCustomer(String stringCustomer,@PathParam("customerId") String customerId){

        Customer customer = null;

        try {
            customer = gson.fromJson(stringCustomer,Customer.class);

            Customer oldCustomer = customerService.getCustomerById(new CustomerId(customerId));

            if(oldCustomer == null)
                return Response.status(Response.Status.NOT_FOUND).entity(customerId).build();

            customer.setCustomerId(new CustomerId(customerId));
            
            customerService.addOrUpdateCustomer(customer);

        }catch (NullParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Customer model is wrong").build();
        } catch (IdException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Customer id is not valid").build();
        }catch (JsonSyntaxException je) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Json is not valid").build();
        }
        return Response.accepted().entity(gson.toJson(customer)).build();
    }

    @DELETE
    @Path("/{customerId}")
    public Response deleteCustomer(@PathParam("customerId") String customerId){
        Customer customer = null;
        try {
            customer = customerService.removeCustomer(new CustomerId(customerId));
            if(customer == null)
                return Response.status(Response.Status.NOT_FOUND).entity("Customer is not found by id = "+ customerId).build();
        }catch (NullParameterException e) {
            return Response.serverError().entity("Customer id is null").build();
        } catch (IdException e) {
            return Response.serverError().entity("Customer id is not valid").build();
        }catch (JsonSyntaxException je) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Json is not valid").build();
        } catch (AccountException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.accepted().entity(gson.toJson(customer)).build();
    }


    private void addFakeCustomerData() throws PhoneNumberException, NullParameterException, IdException {
        Customer customer = new Customer();
        customer.setCustomerName("burak");
        customer.setCustomerSurname("cabuk");
        customer.setCustomerPhone(new PhoneNumber(530,250,400));
        customer.setCustomerId(new CustomerId(IdGenerator.generateCustomerId()));
        customer.setAttendDate(Calendar.getInstance().getTime());
        customerService.addOrUpdateCustomer(customer);

        Customer customer1 = new Customer();
        customer1.setCustomerName("burak1");
        customer1.setCustomerSurname("cabuk1");
        customer1.setCustomerPhone(new PhoneNumber(531,251,401));
        customer1.setCustomerId(new CustomerId(IdGenerator.generateCustomerId()));
        customer1.setAttendDate(Calendar.getInstance().getTime());
        customerService.addOrUpdateCustomer(customer1);

        Customer customer2 = new Customer();
        customer2.setCustomerName("burak2");
        customer2.setCustomerSurname("cabuk2");
        customer2.setCustomerPhone(new PhoneNumber(532,252,402));
        customer2.setCustomerId(new CustomerId(IdGenerator.generateCustomerId()));
        customer2.setAttendDate(Calendar.getInstance().getTime());
        customerService.addOrUpdateCustomer(customer2);
    }
}
