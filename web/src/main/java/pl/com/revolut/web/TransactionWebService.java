package pl.com.revolut.web;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.Logger;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.impl.AccountServiceImpl;
import pl.com.revolut.impl.CustomerServiceImpl;
import pl.com.revolut.impl.TransactionServiceImpl;
import pl.com.revolut.model.Transaction;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.CustomerId;
import pl.com.revolut.model.identifier.TransactionId;
import pl.com.revolut.service.AccountService;
import pl.com.revolut.service.CustomerService;
import pl.com.revolut.service.TransactionService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionWebService {
    private final TransactionService transactionService = TransactionServiceImpl.getTransactionServiceInstance();
    private final AccountService accountService = AccountServiceImpl.getAccountServiceInstance();
    private final CustomerService customerService = CustomerServiceImpl.getCustomerServiceInstance();
    private static final Logger log = Logger.getLogger(AccountWebService.class);
    private Gson gson = new Gson();

    @GET
    public Response getAllTransactions(){
        String transactionJsonList = gson.toJson(transactionService.getAllTransactions());
        log.debug(transactionJsonList);
        return Response.ok(transactionJsonList).build();
    }


    @GET
    @Path("/{transactionId}")
    public Response getTransactionByTransactionId(@PathParam("transactionId") String transactionId){
        Transaction transaction = null;
        try {
            transaction = transactionService.getTransactionById(new TransactionId(transactionId));
            if(transaction == null)
                return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NullParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Transaction id con not be null").build();
        }catch (IdException ie) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Transaction id is not valid").build();
        }catch (JsonSyntaxException je) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Json is not valid").build();
        }
        return Response.ok(gson.toJson(transaction)).build();
    }

    @GET
    @Path("/{accountId}")
    public Response getAllTransactionsByAccountId(@PathParam("accountId") String accountId){
        String transactionJsonList = null;
        try {
            transactionJsonList = gson.toJson(accountService.getTransactionsOfAccount(new AccountId(accountId)));
        } catch (NullParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Transaction id con not be null").build();
        } catch (IdException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Transaction id is not valid").build();
        }
        log.debug(transactionJsonList);
        return Response.ok(transactionJsonList).build();
    }

    @GET
    @Path("/{customerId}")
    public Response getAllTransactionsByCustomerId(@PathParam("customerId") String customerId){
        String transactionJsonList = null;
        try {
            List<AccountId> accountIdList = customerService.getCustomerAccounts(new CustomerId(customerId));
            List<Transaction> transactionList = new ArrayList<>();
            for (AccountId accountId : accountIdList)
            {
                List<TransactionId> transactionIdList = accountService.getTransactionsOfAccount(accountId);
                for (TransactionId transactionId : transactionIdList)
                {
                    Transaction transaction = transactionService.getTransactionById(transactionId);
                    if(transaction != null)
                        transactionList.add(transaction);
                }
            }
            transactionJsonList = gson.toJson(transactionList);
        } catch (NullParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Transaction id con not be null").build();
        } catch (IdException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Transaction id is not valid").build();
        }
        log.debug(transactionJsonList);
        return Response.ok(transactionJsonList).build();
    }


}
