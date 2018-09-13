package pl.com.revolut.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.exception.*;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.TransactionId;
import pl.com.revolut.service.AccountService;
import pl.com.revolut.service.CustomerService;
import pl.com.revolut.service.TransactionService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountServiceImplTest {
    public static List<Account> accountList = new ArrayList<>();
    private static CustomerService customerService = null;
    private AccountService accountService = null;
    private TransactionService transactionService = null;

    public static List<Account> createMockAccount(int count) throws NullParameterException, IdException, PhoneNumberException {
        CustomerServiceImplTest.createMockCustomer(1);

        for (int i = 0 ;i< count;i ++) {
            Account account = new Account();
            String accIdStr = IdGenerator.generateAccountId();
            account.setAccountId(new AccountId(accIdStr));
            account.setCurrentBalance(new BigDecimal(100.0));
            account.setCustomerId(CustomerServiceImplTest.customerList.get(0).getCustomerId());
            account.setOpenDate(new Date());
            accountList.add(account);

        }

        return accountList;

    }

    @Before
    public void setup() throws PhoneNumberException, NullParameterException, IdException, AccountException, TransactionException {
        customerService = CustomerServiceImpl.getCustomerServiceInstance();
        accountService = AccountServiceImpl.getAccountServiceInstance();
        transactionService = TransactionServiceImpl.getTransactionServiceInstance();
        accountService.setCustomerService(customerService);
        CustomerServiceImplTest.setCustomerService(customerService);
        transactionService.setAccountService(accountService);
        AccountServiceImplTest.accountList.clear();
        transactionService.removeAllTransactions();
        accountService.removeAllAccounts();
    }

    @Test
    public void addOrUpdateAccountTest() throws PhoneNumberException, IdException, NullParameterException {

        //ADD ACCOUNT
        List<Account> accounts = createMockAccount(1);
        accountService.addOrUpdateAccount(accounts.get(0));
        List<Account>accountList = accountService.getAllAccounts();
        Assertions.assertEquals(accountList,accounts);


        //UPDATE ACCOUNT
        Account account = accounts.get(0);
        account.setOpenDate(new Date());

        accountService.addOrUpdateAccount(account);
        accountList = accountService.getAllAccounts();
        Assertions.assertEquals(accountList,accounts);

    }


    @Test
    public void removeAccount() throws PhoneNumberException, IdException, NullParameterException, AccountException {


        List<Account> accounts = createMockAccount(1);
        accountService.addOrUpdateAccount(accounts.get(0));
        Assertions.assertEquals(accountService.getAllAccounts().size(), 1);
        accountService.removeAccount(accounts.get(0).getAccountId());
        Assertions.assertEquals(accountService.getAllAccounts().size(), 0);
        List<Account> accountList1 = accountService.getAllAccounts();
        Assertions.assertNotEquals(accountList1,accounts);

    }

    @Test(expected = AccountException.class)
    public void removeAccountWithExceptionTest() throws PhoneNumberException, IdException, NullParameterException, AccountException {


        List<Account> accounts = createMockAccount(1);
        accountService.removeAccount(accounts.get(0).getAccountId());

    }

    @Test
    public void getAccountByIdTest() throws PhoneNumberException, IdException, NullParameterException, AccountException {


        List<Account> accounts = createMockAccount(1);
        accountService.addOrUpdateAccount(accounts.get(0));
        Assertions.assertEquals(accountService.getAccountById(accounts.get(0).getAccountId()),accounts.get(0));

    }


    @Test
    public void addTransactionToAccountTest() throws PhoneNumberException, IdException, NullParameterException, AccountException {


        List<Account> accounts = createMockAccount(1);
        TransactionId transactionId = new TransactionId("TRA-1");
        accountService.addOrUpdateAccount(accounts.get(0));
        accountService.addTransactionToAccount(accounts.get(0).getAccountId(),transactionId);
        List<TransactionId> transactionIds = accountService.getTransactionsOfAccount(accounts.get(0).getAccountId());
        Assertions.assertEquals(transactionIds.get(0),transactionId);
        accountService.removeAccount(accounts.get(0).getAccountId());


    }


    @Test(expected = AccountException.class)
    public void addTransactionToAccountTwiceTest() throws PhoneNumberException, IdException, NullParameterException, AccountException {


        List<Account> accounts = createMockAccount(1);
        TransactionId transactionId = new TransactionId("TRA-1");
        accountService.addTransactionToAccount(accounts.get(0).getAccountId(),transactionId);
        accountService.addTransactionToAccount(accounts.get(0).getAccountId(),transactionId);
        List<TransactionId> transactionIds = accountService.getTransactionsOfAccount(accounts.get(0).getAccountId());
        Assertions.assertEquals(transactionIds.get(0),transactionId);
    }

    @Test
    public void removeTransactionFromAccountTest() throws PhoneNumberException, IdException, NullParameterException, AccountException {
        List<Account> accounts = createMockAccount(1);
        TransactionId transactionId = new TransactionId("TRA-1");
        accountService.addOrUpdateAccount(accounts.get(0));
        accountService.addTransactionToAccount(accounts.get(0).getAccountId(),transactionId);
        Assertions.assertEquals(accountService.getTransactionsOfAccount(accounts.get(0).getAccountId()).size(),1);
        accountService.removeTransactionFromAccount(transactionId,accounts.get(0).getAccountId());
        Assertions.assertEquals(accountService.getTransactionsOfAccount(accounts.get(0).getAccountId()).size(),0);
    }

    @Test(expected = AccountException.class)
    public void removeTransactionFromAccountTwiceTest() throws PhoneNumberException, IdException, NullParameterException, AccountException {
        List<Account> accounts = createMockAccount(1);
        TransactionId transactionId = new TransactionId("TRA-1");
        accountService.addTransactionToAccount(accounts.get(0).getAccountId(),transactionId);
        Assertions.assertEquals(accountService.getTransactionsOfAccount(accounts.get(0).getAccountId()).size(),1);
        accountService.removeTransactionFromAccount(transactionId,accounts.get(0).getAccountId());
        accountService.removeTransactionFromAccount(transactionId,accounts.get(0).getAccountId());
        Assertions.assertEquals(accountService.getTransactionsOfAccount(accounts.get(0).getAccountId()).size(),0);
    }


    @Test(expected = AccountException.class)
    public void getTransacitonOfAccountExceptionTest() throws NullParameterException, IdException, AccountException {
        accountService.getTransactionsOfAccount(new AccountId("ACC-1"));
    }


}
