package pl.com.revolut.web;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.revolut.exception.AccountException;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.service.AccountService;
import pl.com.revolut.service.CustomerService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Controller
@RequestMapping("/accounts")
@Slf4j
public class AccountWebService {

    private AccountService accountService;
    private CustomerService customerService;

    @Autowired
    public AccountWebService(AccountService accountService ,CustomerService customerService)
    {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    private Gson gson = new Gson();

    @GetMapping
    public ResponseEntity getAllAccounts(){
        String accountListJson = gson.toJson(accountService.getAllAccounts());
        log.debug(accountListJson);
        return ResponseEntity.ok(accountListJson);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity getAccountByAccountId(@PathVariable("accountId") String accountId){
        Account account;
        try {
            account = accountService.getAccountById(new AccountId(accountId));
            if(account == null)
                return ResponseEntity.notFound().build();
        } catch (NullParameterException | IdException |JsonSyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(gson.toJson(account));
    }


    @PostMapping
    public ResponseEntity createAccount(@RequestBody String stringAccount){
        URI uri;
        try {
            Account account = gson.fromJson(stringAccount,Account.class);
            if(account.getAccountId() != null)
                return ResponseEntity.notFound().build();

            String accountId = IdGenerator.generateAccountId();
            account.setAccountId(new AccountId(accountId));
            Customer customer = customerService.getCustomerById(account.getCustomerId());
            if(customer == null)
                return ResponseEntity.badRequest().build();
            accountService.addOrUpdateAccount(account);
            uri = ServletUriComponentsBuilder.fromCurrentRequest().path(
                    "/{id}").buildAndExpand(customer.getCustomerId()).toUri();
        }catch (NullParameterException | IdException |JsonSyntaxException e) {
        return ResponseEntity.badRequest().build();
    }


        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{accountId}")
    public ResponseEntity updateAccount(@RequestBody  String stringAccount,@PathVariable("accountId") String accountId) {

        Account account;

        try {
            account = gson.fromJson(stringAccount, Account.class);

            Account oldAccount = accountService.getAccountById(new AccountId(accountId));

            if (oldAccount == null)
                return ResponseEntity.notFound().build();

            account.setAccountId(new AccountId(accountId));

            Customer customer = customerService.getCustomerById(account.getCustomerId());
            if (customer == null)
                return ResponseEntity.badRequest().build();

            accountService.addOrUpdateAccount(account);

        } catch (NullParameterException | IdException | JsonSyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(gson.toJson(account));

    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity deleteAccount(@PathVariable("accountId") String accountId) {
        Account account;
        try {
            account = accountService.removeAccount(new AccountId(accountId));
            if (account == null)
                return ResponseEntity.notFound().build();
        } catch (NullParameterException | IdException | JsonSyntaxException | AccountException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(gson.toJson(account));
    }


}
