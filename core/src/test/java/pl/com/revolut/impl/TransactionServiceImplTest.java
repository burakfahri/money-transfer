package pl.com.revolut.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
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


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {AccountServiceImpl.class,CustomerServiceImpl.class,TransactionServiceImpl.class})
public class TransactionServiceImplTest extends AbstractServiceImplTest{
    @Autowired private CustomerService customerService ;
    @Autowired private AccountService accountService ;
    @Autowired private TransactionService transactionService ;




    @Before
    public void setup() throws PhoneNumberException, NullParameterException, IdException {
    }


    @Test
    public void withDrawTest() throws PhoneNumberException, IdException, NullParameterException, AccountException, AccountServiceException, TransactionException {
        createMockAccount(1);
        accountService.addOrUpdateAccount(accountList.get(0));
        Transaction transaction = transactionService.withDraw(accountList.get(0).getAccountId(),new BigDecimal(100.0));
        Assertions.assertEquals(accountService.getAccountById(accountList.get(0).getAccountId()).getCurrentBalance(),new BigDecimal(0));
        accountService.removeAccount(accountList.get(0).getAccountId());
        accountList = new ArrayList<>();
        Assertions.assertEquals(transaction,transactionService.getTransactionById(transaction.getTransactionId()));
    }

    @Test
    public void depositTest() throws PhoneNumberException, IdException, NullParameterException, AccountException, AccountServiceException, TransactionException {
        createMockAccount(1);
        accountService.addOrUpdateAccount(accountList.get(0));
        Transaction transaction = transactionService.deposit(accountList.get(0).getAccountId(),new BigDecimal(100.0));
        Assertions.assertEquals(accountService.getAccountById(accountList.get(0).getAccountId()).getCurrentBalance(),new BigDecimal(200));
        accountService.removeAccount(accountList.get(0).getAccountId());
        accountList = new ArrayList<>();
        Assertions.assertEquals(transaction,transactionService.getTransactionById(transaction.getTransactionId()));
    }

    @Test
    public void transferTest() throws PhoneNumberException, IdException, NullParameterException, AccountException, AccountServiceException, TransactionException {
        createMockAccount(2);
        System.out.println(accountList.get(0));
        System.out.println(accountList.get(1));
        accountService.addOrUpdateAccount(accountList.get(0));
        accountService.addOrUpdateAccount(accountList.get(1));
        transactionService.transfer(accountList.get(0).getAccountId(),accountList.get(1).getAccountId(),new BigDecimal(100.0),"EXPLANATION1");
        Assertions.assertEquals(accountService.getAccountById(accountList.get(1).getAccountId()).getCurrentBalance(),new BigDecimal(200));
        Assertions.assertEquals(accountService.getAccountById(accountList.get(0).getAccountId()).getCurrentBalance(),new BigDecimal(0));
        accountService.removeAccount(accountList.get(0).getAccountId());
        accountService.removeAccount(accountList.get(1).getAccountId());
        accountList = new ArrayList<>();
    }





}
