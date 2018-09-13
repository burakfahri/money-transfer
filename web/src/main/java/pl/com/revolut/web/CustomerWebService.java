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

}
