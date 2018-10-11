package pl.com.revolut.web;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.com.revolut.exception.*;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("/transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Controller
@Slf4j
public class TransactionWebService {

    private AccountService accountService;
    private CustomerService customerService;
    private TransactionService transactionService;

    @Autowired
    public TransactionWebService(AccountService accountService ,CustomerService customerService,TransactionService transactionService)
    {
        this.accountService = accountService;
        this.customerService = customerService;
        this.transactionService = transactionService;
    }

    private Gson gson = new Gson();

    @GetMapping
    public ResponseEntity getAllTransactions(){
        String transactionJsonList = gson.toJson(transactionService.getAllTransactions());
        log.debug(transactionJsonList);
        return ResponseEntity.ok(transactionJsonList);
    }


    @GetMapping("/{transactionId}")
    public ResponseEntity getTransactionByTransactionId(@PathVariable("transactionId") String transactionId){
        Transaction transaction;
        try {
            transaction = transactionService.getTransactionById(new TransactionId(transactionId));
            if(transaction == null)
                return ResponseEntity.notFound().build();
        } catch (NullParameterException | IdException | JsonSyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(gson.toJson(transaction));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity getAllTransactionsByAccountId(@PathVariable("accountId") String accountIdString){
        String transactionJsonList;
        try {
            
            AccountId accountId = new AccountId(accountIdString);
            Account account = accountService.getAccountById(accountId);
            
            if(account == null)
                return ResponseEntity.notFound().build();

            List<TransactionId> transactionIdList = accountService.getTransactionsOfAccount(accountId);
            List<Transaction> transactionList = new ArrayList<>();
            
            for (TransactionId transactionId : transactionIdList) {
                try {
                    Transaction transaction = transactionService.getTransactionById(transactionId);
                    transactionList.add(transaction);
                } catch (NullParameterException e) {
                    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            transactionJsonList = gson.toJson(transactionList);
        } catch (NullParameterException | IdException | JsonSyntaxException | AccountException e) {
            return ResponseEntity.badRequest().build();
        }
        log.debug(transactionJsonList);
        return ResponseEntity.ok(transactionJsonList);
    }

    @GetMapping("customer/{customerId}")
    public ResponseEntity getAllTransactionsByCustomerId(@PathVariable("customerId") String customerIdStr){
        String transactionJsonList;
        try {
            CustomerId customerId = new CustomerId(customerIdStr);
            
            Customer customer = customerService.getCustomerById(customerId);
            if(customer == null)
                return ResponseEntity.notFound().build();


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
        } catch (NullParameterException | IdException | JsonSyntaxException | AccountException e) {
            return ResponseEntity.badRequest().build();
        }

        log.debug(transactionJsonList);
        return ResponseEntity.ok(transactionJsonList);
    }

    @PostMapping("/account/{accountId}/withdraw/{amount}")
    public ResponseEntity withDraw(@PathVariable("accountId") String accountId, @PathVariable("amount") BigDecimal amount){
        String transactionJson;
        try {
            Transaction transaction  = transactionService.withDraw(new AccountId(accountId),amount);
            transactionJson = gson.toJson(transaction);
        } catch (NullParameterException | TransactionException e) {
            return ResponseEntity.badRequest().build();
        } catch (IdException | AccountException e) {
            return ResponseEntity.notFound().build();
        }  catch (AccountServiceException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.debug(transactionJson);
        return ResponseEntity.ok(transactionJson);
    }


    @PostMapping("account/{accountId}/deposit/{amount}")
    public ResponseEntity deposit(@PathVariable("accountId") String accountId, @PathVariable("amount") BigDecimal amount){
        String transactionJson;
        try {
            Transaction transaction  = transactionService.deposit(new AccountId(accountId),amount);
            transactionJson = gson.toJson(transaction);
        } catch (IdException | AccountException | NullParameterException e) {
            return ResponseEntity.badRequest().build();
        }  catch (AccountServiceException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.debug(transactionJson);
        return ResponseEntity.ok(transactionJson);
    }


    @PostMapping("/transfer/from/{senderAccountId}/to/{receiverAccountId}/amount/{amount}/comment/{comment}")
    public ResponseEntity transfer(@PathVariable("senderAccountId") String senderAccountId,@PathVariable("receiverAccountId")
            String receiverAccountId ,@PathVariable("amount") BigDecimal amount,@PathVariable("comment") String comment){
        String transactionJson;
        try {
            Transaction transaction  = transactionService.transfer(new AccountId(senderAccountId),new AccountId(receiverAccountId)
                    ,amount,comment);
            transactionJson = gson.toJson(transaction);
        } catch (IdException | AccountException | NullParameterException | TransactionException e) {
            return ResponseEntity.badRequest().build();
        }  catch (AccountServiceException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.debug(transactionJson);
        return ResponseEntity.ok(transactionJson);
    }


}
