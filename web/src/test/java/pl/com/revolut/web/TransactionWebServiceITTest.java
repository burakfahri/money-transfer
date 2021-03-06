package pl.com.revolut.web;

import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.exception.*;
import pl.com.revolut.impl.AccountServiceImpl;
import pl.com.revolut.impl.CustomerServiceImpl;
import pl.com.revolut.impl.TransactionServiceImpl;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.Transaction;
import pl.com.revolut.model.TransactionType;
import pl.com.revolut.model.identifier.CustomerId;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TransactionWebService.class, AccountServiceImpl.class, CustomerServiceImpl.class, TransactionServiceImpl.class})
@EnableAutoConfiguration
public class TransactionWebServiceITTest extends WebServiceTest{

    @Before
    public void before() throws NullParameterException, AccountException, TransactionException {
        transactionService.removeAllTransactions();
        transactionService.removeAllTransactions();
        accountService.removeAllAccounts();
        customerService.removeAllCustomers();
    }

    @Test
    public void testDeposit() throws URISyntaxException, IOException, PhoneNumberException, IdException, NullParameterException {
       List<Account> mockAccountList = createMockAccountListForTransaction(1);
       Account mockAccount = mockAccountList.get(0);
       HttpResponse httpResponse = executeHttpEntityEnclosingRequestBase("/transactions/account/"+
               mockAccount.getAccountId().getId()+"/deposit/100",null, HttpPost.METHOD_NAME);
       assertEquals(httpResponse.getStatusLine().getStatusCode() , 200);

       httpResponse = executeHttpRequestBase("/transactions/account/"+
               mockAccount.getAccountId().getId(),HttpGet.METHOD_NAME);
        assertEquals(httpResponse.getStatusLine().getStatusCode() , 200);
        String jsonString = EntityUtils.toString(httpResponse.getEntity());
        List<Transaction> transactionList = (ArrayList<Transaction>) gson.fromJson(jsonString,new TypeToken<ArrayList<Transaction>>(){}.getType());

        assertEquals(transactionList.size(),1);
        assertEquals(transactionList.get(0).getAmount(),new BigDecimal(100));
        assertEquals(transactionList.get(0).getTransactionType(), TransactionType.DEPOSIT);
    }

    @Test
    public void testWithDraw() throws URISyntaxException, IOException, PhoneNumberException, IdException, NullParameterException {
        List<Account> mockAccountList = createMockAccountListForTransaction(1);
        Account mockAccount = mockAccountList.get(0);
        HttpResponse httpResponse = executeHttpEntityEnclosingRequestBase("/transactions/account/"+
                mockAccount.getAccountId().getId()+"/withdraw/100",null, HttpPost.METHOD_NAME);
        assertEquals(httpResponse.getStatusLine().getStatusCode() , 200);


        httpResponse = executeHttpRequestBase("/transactions/customer/"+
                customerService.getAllCustomers().get(0).getCustomerId().getId(),HttpGet.METHOD_NAME);
        assertEquals(httpResponse.getStatusLine().getStatusCode() , 200);
        String jsonString = EntityUtils.toString(httpResponse.getEntity());
        List<Transaction> transactionList = (ArrayList<Transaction>) gson.fromJson(jsonString,new TypeToken<ArrayList<Transaction>>(){}.getType());

        assertEquals(transactionList.size(),1);
        assertEquals(transactionList.get(0).getAmount(),new BigDecimal(100));
        assertEquals(transactionList.get(0).getTransactionType(), TransactionType.WITHDRAW);
    }

    @Test
    public void testTransfer() throws URISyntaxException, IOException, PhoneNumberException, IdException, NullParameterException {
        List<Account> mockAccountList = createMockAccountListForTransaction(2);
        Account mockSenderAccount = mockAccountList.get(0);
        Account mockReceiverAccount = mockAccountList.get(0);
        HttpResponse httpResponse = executeHttpEntityEnclosingRequestBase("/transactions/transfer/from/"+
                mockSenderAccount.getAccountId().getId()+"/to/"+
                        mockReceiverAccount.getAccountId().getId()+
                "/amount/100/comment/EXPL1",null, HttpPost.METHOD_NAME);
        assertEquals(httpResponse.getStatusLine().getStatusCode() , 200);


        httpResponse = executeHttpRequestBase("/transactions",HttpGet.METHOD_NAME);
        assertEquals(httpResponse.getStatusLine().getStatusCode() , 200);
        String jsonString = EntityUtils.toString(httpResponse.getEntity());
        List<Transaction> transactionList = (ArrayList<Transaction>) gson.fromJson(jsonString,new TypeToken<ArrayList<Transaction>>(){}.getType());

        assertEquals(transactionList.size(),1);
        assertEquals(transactionList.get(0).getAmount(),new BigDecimal(100));
        assertEquals(transactionList.get(0).getTransactionType(), TransactionType.TRANSFER);
        assertEquals(transactionList.get(0).getSenderAccountId(), mockSenderAccount.getAccountId());
        assertEquals(transactionList.get(0).getReceiverAccountId(), mockReceiverAccount.getAccountId());


        httpResponse = executeHttpRequestBase("/transactions/"+ transactionList.get(0).getTransactionId().getId(),HttpGet.METHOD_NAME);
        assertEquals(httpResponse.getStatusLine().getStatusCode() , 200);
        jsonString = EntityUtils.toString(httpResponse.getEntity());
        Transaction transaction = (Transaction) gson.fromJson(jsonString,Transaction.class);

        assertEquals(transactionList.get(0),transaction);
    }

}
