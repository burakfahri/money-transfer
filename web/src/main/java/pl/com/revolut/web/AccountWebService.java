package pl.com.revolut.web;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import pl.com.revolut.common.exception.IdException;
import pl.com.revolut.common.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.common.utils.web.WebUtils;
import pl.com.revolut.impl.AccountServiceImpl;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.service.AccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;

@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountWebService {
    private final AccountService accountService = AccountServiceImpl.getAccountServiceInstance();

    private static final Logger log = Logger.getLogger(AccountWebService.class);
    private Gson gson = new Gson();

    @GET
    @Path("")
    public Response getAllAccounts(){

        String accountListJson = gson.toJson(accountService.getAllAccounts());
        log.debug(accountListJson);
        return Response.ok(accountListJson).build();
    }

    @GET
    @Path("/{accountId}")
    public Response getAllAccounts(@PathParam("accountId") String accountId){
        Boolean isIdValid = null;
        Account account = null;
        try {
            isIdValid = IdGenerator.validateAccountId(accountId);

            if(!isIdValid)
                return Response.serverError().entity("Account id is not acceptable").build();

            account = accountService.getAccountById(new AccountId(accountId));
            if(account == null)
                return Response.noContent().build();
        } catch (NullParameterException e) {
            return Response.serverError().entity("Account id is not acceptable").build();
        }catch (IdException ie) {
            return Response.serverError().entity("Id is not valid").build();
        }
        return Response.ok(gson.toJson(account)).build();
    }


    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(String stringAccount,@Context UriInfo uriInfo){

        Account account = null;
        URI uri = null;
        try {
            log.info(uriInfo);
            account = gson.fromJson(stringAccount,Account.class);
            String accountId = IdGenerator.generateAccountId();
            account.setAccountId(new AccountId(accountId));
            uri = WebUtils.generateUri(uriInfo,accountId);
            accountService.addOrUpdateAccount(account);
        }catch (NullParameterException e) {
            return Response.serverError().entity("Account model is wrong").build();
        }catch (IdException ie) {
            return Response.serverError().entity("Id is not valid").build();
        }
        return Response.created(uri).build();
    }

    @PUT
    @Path("/{accountId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(String stringAccount,@PathParam("accountId") String accountId){

        Account account = null;
        Boolean isIdValid = null;
        try {
            account = gson.fromJson(stringAccount,Account.class);
            isIdValid = IdGenerator.validateAccountId(accountId);

            if(!isIdValid)
                return Response.serverError().entity("Account id is not acceptable").build();

            Account oldAccount = accountService.getAccountById(new AccountId(accountId));

            if(oldAccount == null)
                return Response.status(Response.Status.NOT_FOUND).entity(accountId).build();

            account.setAccountId(new AccountId(accountId));

            accountService.addOrUpdateAccount(account);

        }catch (NullParameterException e) {
            return Response.serverError().entity("Account model is wrong").build();
        } catch (IdException e) {
            return Response.serverError().entity("Id is not valid").build();
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
        } catch (IdException e) {
            return Response.serverError().entity("Id is not valid").build();
        }
        return Response.accepted().build();
    }


}
