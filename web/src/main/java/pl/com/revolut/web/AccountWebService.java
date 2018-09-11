package pl.com.revolut.web;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.Logger;
import pl.com.revolut.exception.AccountException;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.common.utils.web.WebUtils;
import pl.com.revolut.impl.AccountServiceImpl;
import pl.com.revolut.impl.CustomerServiceImpl;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.service.AccountService;
import pl.com.revolut.service.CustomerService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;

@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountWebService {
    private final AccountService accountService = AccountServiceImpl.getAccountServiceInstance();
    private final CustomerService customerService = CustomerServiceImpl.getCustomerServiceInstance();
    private static final Logger log = Logger.getLogger(AccountWebService.class);
    private Gson gson = new Gson();

    @GET
    public Response getAllAccounts(){
        String accountListJson = gson.toJson(accountService.getAllAccounts());
        log.debug(accountListJson);
        return Response.ok(accountListJson).build();
    }

    @GET
    @Path("/{accountId}")
    public Response getAccountByAccountId(@PathParam("accountId") String accountId){
        Boolean isIdValid = null;
        Account account = null;
        try {
            account = accountService.getAccountById(new AccountId(accountId));
            if(account == null)
                return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NullParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account id is not acceptable").build();
        }catch (IdException ie) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Id is not valid").build();
        }catch (JsonSyntaxException je) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Json is not valid").build();
        }
        return Response.ok(gson.toJson(account)).build();
    }


    @POST
    public Response createAccount(String stringAccount,@Context UriInfo uriInfo){

        Account account = null;
        URI uri = null;
        try {
            log.info(uriInfo);
            account = gson.fromJson(stringAccount,Account.class);
            if(account.getAccountId() == null)
                return Response.status(Response.Status.BAD_REQUEST).entity("Accound id must " +
                        "be null while creating new account").build();

            String accountId = IdGenerator.generateAccountId();
            account.setAccountId(new AccountId(accountId));
            Customer customer = customerService.getCustomerById(account.getCustomerId());
            if(customer == null)
                return Response.status(Response.Status.BAD_REQUEST).entity("Customer id is wrong").build();
            uri = WebUtils.generateUri(uriInfo,accountId);
            accountService.addOrUpdateAccount(account);
        }catch (NullParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account model is wrong").build();
        }catch (IdException ie) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Id is not valid").build();
        }catch (JsonSyntaxException je) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Json is not valid").build();
        }

        return Response.created(uri).build();
    }

    @PUT
    @Path("/{accountId}")
    public Response updateAccount(String stringAccount,@PathParam("accountId") String accountId){

        Account account = null;

        try {
            account = gson.fromJson(stringAccount,Account.class);

            Account oldAccount = accountService.getAccountById(new AccountId(accountId));

            if(oldAccount == null)
                return Response.status(Response.Status.NOT_FOUND).entity(accountId).build();

            account.setAccountId(new AccountId(accountId));

            Customer customer = customerService.getCustomerById(account.getCustomerId());
            if(customer == null)
                return Response.status(Response.Status.BAD_REQUEST).entity("Customer id is wrong").build();

            accountService.addOrUpdateAccount(account);

        }catch (NullParameterException e) {
            return Response.serverError().entity("Account model is wrong").build();
        } catch (IdException e) {
            return Response.serverError().entity("Account id is not valid").build();
        }catch (JsonSyntaxException je) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Json is not valid").build();
        }
        return Response.accepted().entity(account).build();
    }

    @DELETE
    @Path("/{accountId}")
    public Response deleteAccount(@PathParam("accountId") String accountId){
        Account account = null;
        try {
            account = accountService.removeAccount(new AccountId(accountId));
            if(account == null)
                return Response.status(Response.Status.NOT_FOUND).entity("Account is not found "+ accountId).build();
        }catch (NullParameterException e) {
            return Response.serverError().entity("Account id is null").build();
        } catch (IdException | AccountException e) {
            return Response.serverError().entity("Id is not valid").build();
        }catch (JsonSyntaxException je) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Json is not valid").build();
        }
        return Response.accepted().entity(account).build();
    }

}
