package pl.com.revolut.web;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.Logger;
import pl.com.revolut.exception.*;
import pl.com.revolut.impl.AccountServiceImpl;
import pl.com.revolut.impl.CustomerServiceImpl;
import pl.com.revolut.impl.TransactionServiceImpl;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.Customer;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
            return Response.status(Response.Status.BAD_REQUEST).entity("Transaction id can not be null").build();
        }catch (IdException ie) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Transaction id is not valid").build();
        }catch (JsonSyntaxException je) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Json is not valid").build();
        }
        return Response.ok(gson.toJson(transaction)).build();
    }

    @GET
    @Path("/account/{accountId}")
    public Response getAllTransactionsByAccountId(@PathParam("accountId") String accountIdString){
        String transactionJsonList = null;
        try {
            
            AccountId accountId = new AccountId(accountIdString);
            Account account = accountService.getAccountById(accountId);
            
            if(account == null)
                return Response.status(Response.Status.NOT_FOUND).entity("Account id does not exist").build();

            List<TransactionId> transactionIdList = accountService.getTransactionsOfAccount(accountId);
            List<Transaction> transactions = new ArrayList<>();
            
            for (TransactionId transactionId : transactionIdList) {
                try {
                    Transaction transaction = transactionService.getTransactionById(transactionId);
                    transactions.add(transaction);
                } catch (NullParameterException e) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("There is an error while " +
                            "fetching the transaction of transactionId = " + transactionId).build();
                }
            }
            transactionJsonList = gson.toJson(transactions);
        } catch (NullParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account id can not be null").build();
        } catch (IdException | AccountException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account id is not valid").build();
        }

        log.debug(transactionJsonList);
        return Response.ok(transactionJsonList).build();
    }

    @GET
    @Path("customer/{customerId}")
    public Response getAllTransactionsByCustomerId(@PathParam("customerId") String customerIdStr){
        String transactionJsonList = null;
        try {
            CustomerId customerId = new CustomerId(customerIdStr);
            
            Customer customer = customerService.getCustomerById(customerId);
            if(customer == null)
                return Response.status(Response.Status.NOT_FOUND).entity("Customer id does not exist").build();


            List<AccountId> accountIdList = customerService.getCustomerAccounts(customerId);
            Map<TransactionId, Transaction> transactionMap = new ConcurrentHashMap<>();
            for (AccountId accountId : accountIdList)
            {
                List<TransactionId> transactionIdList = accountService.getTransactionsOfAccount(accountId);
                for (TransactionId transactionId : transactionIdList)
                {
                    Transaction transaction = transactionService.getTransactionById(transactionId);
                    if(transaction != null)
                        transactionMap.put(transactionId,transaction);
                }
            }
            transactionJsonList = gson.toJson(transactionMap.values());
        } catch (NullParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Customer id can not be null").build();
        } catch (IdException | AccountException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Customer id is not valid").build();
        }

        log.debug(transactionJsonList);
        return Response.ok(transactionJsonList).build();
    }

    @POST
    @Path("/account/{accountId}/withdraw/{amount}")
    public Response withDraw(@PathParam("accountId") String accountId, @PathParam("amount") BigDecimal amount){
        String transactionJson = null;
        try {
            Transaction transaction  = transactionService.withDraw(new AccountId(accountId),amount);
            transactionJson = gson.toJson(transaction);
        } catch (NullParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account id or amount can not be null").build();
        } catch (IdException | AccountException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account id is not valid or does not exist").build();
        }  catch (AccountServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Account Service is not valid").build();
        } catch (TransactionException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account has not got enough money").build();
        }
        log.debug(transactionJson);
        return Response.ok(transactionJson).build();
    }


    @POST
    @Path("account/{accountId}/deposit/{amount}")
    public Response deposit(@PathParam("accountId") String accountId, @PathParam("amount") BigDecimal amount){
        String transactionJson = null;
        try {
            Transaction transaction  = transactionService.deposit(new AccountId(accountId),amount);
            transactionJson = gson.toJson(transaction);
        } catch (NullParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account id or amount can not be null").build();
        } catch (IdException | AccountException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account id or amount are not be valid").build();
        }  catch (AccountServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Account Service is not valid").build();
        }

        log.debug(transactionJson);
        return Response.ok(transactionJson).build();
    }


    @POST
    @Path("/transfer/from/{senderAccountId}/to/{receiverAccountId}/amount/{amount}/comment/{comment}")
    public Response deposit(@PathParam("senderAccountId") String senderAccountId,@PathParam("receiverAccountId")
            String receiverAccountId ,@PathParam("amount") BigDecimal amount,@PathParam("comment") String comment){
        String transactionJson = null;
        try {
            Transaction transaction  = transactionService.transfer(new AccountId(senderAccountId),new AccountId(receiverAccountId)
                    ,amount,comment);
            transactionJson = gson.toJson(transaction);
        } catch (NullParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account ids or amount can not be null")
                    .build();
        } catch (IdException | AccountException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account ids or amount are not be valid").build();
        }  catch (AccountServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Account Service is not valid").build();
        } catch (TransactionException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Sender account has not got enough money").build();
        }
        log.debug(transactionJson);
        return Response.ok(transactionJson).build();
    }


}
