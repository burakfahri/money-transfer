package pl.com.revolut.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.common.utils.impl.ServiceUtils;
import pl.com.revolut.exception.*;
import pl.com.revolut.model.Transaction;
import pl.com.revolut.model.identifier.TransactionId;
import pl.com.revolut.service.AccountService;
import pl.com.revolut.service.CustomerService;
import pl.com.revolut.service.TransactionService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionServiceImplTest {
    public static List<Transaction> transactionList = new ArrayList<>();
    private CustomerService customerService = null;
    private AccountService accountService = null;
    private TransactionService transactionService = null;


    public static List<Transaction> createMockTransaction(int count) throws NullParameterException, IdException, PhoneNumberException {
        ServiceUtils.checkParameters(count);
        AccountServiceImplTest.createMockAccount(2);

        for (int i = 0 ;i< count;i ++) {
            Transaction transaction = new Transaction();
            String accIdStr = IdGenerator.generateTransactionId();
            transaction.setTransactionId(new TransactionId(accIdStr));
            transaction.setSenderAccountId(AccountServiceImplTest.accountList.get(0).getAccountId());
            transaction.setReceiverAccountId(AccountServiceImplTest.accountList.get(1).getAccountId());
            transaction.setDate(new Date());
            transaction.setAmount(new BigDecimal(100.0));
            transaction.setExplanation("EXPLANATION"+count);

            transactionList.add(transaction);
        }

        return transactionList;
    }

    @Before
    public void setup() throws PhoneNumberException, NullParameterException, IdException {
        customerService = CustomerServiceImpl.getCustomerServiceInstance();
        accountService = AccountServiceImpl.getAccountServiceInstance();
        transactionService = TransactionServiceImpl.getTransactionServiceInstance();
        accountService.setCustomerService(customerService);
        CustomerServiceImplTest.setCustomerService(customerService);
        transactionService.setAccountService(accountService);
    }


    @Test
    public void withDrawTest() throws PhoneNumberException, IdException, NullParameterException, AccountException, AccountServiceException, TransactionException {
        AccountServiceImplTest.createMockAccount(1);
        accountService.addOrUpdateAccount(AccountServiceImplTest.accountList.get(0));
        Transaction transaction = transactionService.withDraw(AccountServiceImplTest.accountList.get(0).getAccountId(),new BigDecimal(100.0));
        Assertions.assertEquals(accountService.getAccountById(AccountServiceImplTest.accountList.get(0).getAccountId()).getCurrentBalance(),new BigDecimal(0));
        accountService.removeAccount(AccountServiceImplTest.accountList.get(0).getAccountId());
        AccountServiceImplTest.accountList = new ArrayList<>();
        Assertions.assertEquals(transaction,transactionService.getTransactionById(transaction.getTransactionId()));
    }

    @Test
    public void depositTest() throws PhoneNumberException, IdException, NullParameterException, AccountException, AccountServiceException, TransactionException {
        AccountServiceImplTest.createMockAccount(1);
        accountService.addOrUpdateAccount(AccountServiceImplTest.accountList.get(0));
        Transaction transaction = transactionService.deposit(AccountServiceImplTest.accountList.get(0).getAccountId(),new BigDecimal(100.0));
        Assertions.assertEquals(accountService.getAccountById(AccountServiceImplTest.accountList.get(0).getAccountId()).getCurrentBalance(),new BigDecimal(200));
        accountService.removeAccount(AccountServiceImplTest.accountList.get(0).getAccountId());
        AccountServiceImplTest.accountList = new ArrayList<>();
        Assertions.assertEquals(transaction,transactionService.getTransactionById(transaction.getTransactionId()));
    }

    @Test
    public void transferTest() throws PhoneNumberException, IdException, NullParameterException, AccountException, AccountServiceException, TransactionException {
        AccountServiceImplTest.createMockAccount(2);
        System.out.println(AccountServiceImplTest.accountList.get(0));
        System.out.println(AccountServiceImplTest.accountList.get(1));
        accountService.addOrUpdateAccount(AccountServiceImplTest.accountList.get(0));
        accountService.addOrUpdateAccount(AccountServiceImplTest.accountList.get(1));
        transactionService.transfer(AccountServiceImplTest.accountList.get(0).getAccountId(),AccountServiceImplTest.accountList.get(1).getAccountId(),new BigDecimal(100.0),"EXPLANATION1");
        Assertions.assertEquals(accountService.getAccountById(AccountServiceImplTest.accountList.get(1).getAccountId()).getCurrentBalance(),new BigDecimal(200));
        Assertions.assertEquals(accountService.getAccountById(AccountServiceImplTest.accountList.get(0).getAccountId()).getCurrentBalance(),new BigDecimal(0));
        accountService.removeAccount(AccountServiceImplTest.accountList.get(0).getAccountId());
        accountService.removeAccount(AccountServiceImplTest.accountList.get(1).getAccountId());
        AccountServiceImplTest.accountList = new ArrayList<>();
    }





}
