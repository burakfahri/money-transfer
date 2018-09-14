package pl.com.revolut.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.exception.AccountException;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.exception.PhoneNumberException;
import pl.com.revolut.exception.TransactionException;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.identifier.AccountId;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Burak Cabuk .
 */
public class AccountWebServiceITTest extends WebServiceTest{
    @Before
    public void before() throws NullParameterException, AccountException, TransactionException {
        transactionService.removeAllTransactions();
        accountService.removeAllAccounts();
        customerService.removeAllCustomers();
    }

    @Test
    public void testGetAllAccounts() throws PhoneNumberException, IdException, NullParameterException, URISyntaxException, IOException {
        List<Account> accountList = createMockAccountList(100);

        for (Account account : accountList) {
            account.setAccountId(new AccountId(IdGenerator.generateAccountId()));
            accountService.addOrUpdateAccount(account);
        }

        HttpResponse response = executeHttpRequestBase("/accounts", HttpGet.METHOD_NAME);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == Response.Status.OK.getStatusCode());
        String jsonString = EntityUtils.toString(response.getEntity());
        List<Account> accounts = gson.fromJson(jsonString, ArrayList.class);
        assertEquals(accounts.size(), accountList.size());
    }

    @Test
    public void testGetAccountByAccountId() throws PhoneNumberException, IdException, NullParameterException, URISyntaxException, IOException {
        List<Account> accountList = createMockAccountList(1);
        AccountId accountId = new AccountId(IdGenerator.generateAccountId());
        for (Account account : accountList) {
            account.setAccountId(accountId);
            accountService.addOrUpdateAccount(account);
        }

        //Success
        HttpResponse response = executeHttpRequestBase("/accounts/"+accountList.get(0).getAccountId().getId(), HttpGet.METHOD_NAME);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == Response.Status.OK.getStatusCode());
        //Bad id
        response = executeHttpRequestBase("/accounts/fail", HttpGet.METHOD_NAME);
        statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == Response.Status.BAD_REQUEST.getStatusCode());
        //Id not found
        AccountId fakeAccountId = new AccountId(IdGenerator.generateAccountId());
        response = executeHttpRequestBase("/accounts/"+fakeAccountId.getId(), HttpGet.METHOD_NAME);
        statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testCreateAccount() throws PhoneNumberException, IdException, NullParameterException, URISyntaxException, IOException {
        List<Account> accountList = createMockAccountList(1);
        //Success
        HttpResponse httpResponse = executeHttpEntityEnclosingRequestBase("/accounts"
                ,gson.toJson(accountList.get(0)), HttpPost.METHOD_NAME);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        assertTrue(statusCode == Response.Status.CREATED.getStatusCode());
        //Corrupt Json
        httpResponse = executeHttpEntityEnclosingRequestBase("/accounts"
                ,"{fail}", HttpPost.METHOD_NAME);
        statusCode = httpResponse.getStatusLine().getStatusCode();
        assertTrue(statusCode == Response.Status.BAD_REQUEST.getStatusCode());
    }
    @Test
    public void testUpdateAccount() throws PhoneNumberException, IdException, NullParameterException, URISyntaxException, IOException {
        List<Account> accountList = createMockAccountList(1);
        for (Account account : accountList) {

            account.setAccountId(new AccountId(IdGenerator.generateAccountId()));
            accountService.addOrUpdateAccount(account);

            HttpResponse httpResponse = executeHttpEntityEnclosingRequestBase("/accounts/" +
                            account.getAccountId().getId(), gson.toJson(account), HttpPut.METHOD_NAME);

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            assertTrue(statusCode == Response.Status.OK.getStatusCode());
        }



    }

    @Test
    public void testDeleteAccount() throws PhoneNumberException, IdException, NullParameterException, URISyntaxException, IOException {
        List<Account> accountList = createMockAccountList(1);
        AccountId accountId = new AccountId(IdGenerator.generateAccountId());

        for (Account account : accountList) {
            account.setAccountId(accountId);
            accountService.addOrUpdateAccount(account);
        }
        HttpResponse httpResponse = executeHttpRequestBase("/accounts/"+accountId.getId()
                , HttpDelete.METHOD_NAME);
        assertTrue(httpResponse.getStatusLine().getStatusCode() == Response.Status.OK.getStatusCode());

    }


}
