package pl.com.revolut.web;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.exception.PhoneNumberException;
import pl.com.revolut.impl.AccountServiceImpl;
import pl.com.revolut.impl.CustomerServiceImpl;
import pl.com.revolut.impl.TransactionServiceImpl;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.PhoneNumber;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.CustomerId;
import pl.com.revolut.service.AccountService;
import pl.com.revolut.service.CustomerService;
import pl.com.revolut.service.TransactionService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WebServiceTest {

    protected static Server server = null;
    protected static HttpClient client ;
    protected static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
    protected URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:8084");
    protected Gson gson = new Gson();
    protected static AccountService accountService = null;
    protected static TransactionService transactionService = null;
    protected static CustomerService customerService = null;


    @BeforeClass
    public static void setup() throws Exception {
        startServer();
        connManager.setDefaultMaxPerRoute(100);
        connManager.setMaxTotal(200);

        client= HttpClients.custom()
                .setConnectionManager(connManager)
                .setConnectionManagerShared(true)
                .build();
        init();
    }

    @AfterClass
    public static void closeClient() throws Exception{
        //server.stop();
        HttpClientUtils.closeQuietly(client);
    }

    public  List<Customer> createMockCustomerList(int count) throws PhoneNumberException, NullParameterException, IdException {
        if(count > 499)
            count = 499;
        List<Customer> customerList = new ArrayList<>();
        for (int i = 0 ;i< count;i ++) {
            Customer customer = new Customer();
            customer.setCustomerName("burak"+ count);
            customer.setCustomerSurname("cabuk"+count);
            customer.setCustomerPhone(new PhoneNumber(530+count, 250+count, 400+count));
            customer.setAttendDate(Calendar.getInstance().getTime());
            customerList.add(customer);
        }
        return customerList;
    }


    public  List<Account> createMockAccountList(int count) throws PhoneNumberException, NullParameterException, IdException {
        List<Customer> customerList = createMockCustomerList(1);
        List<Account> accountList = new ArrayList<>();

        for (Customer customer: customerList) {
            customer.setCustomerId(new CustomerId(IdGenerator.generateCustomerId()));
            customerService.addOrUpdateCustomer(customer);
        }

        for (int i = 0 ;i< count;i ++) {
            Account account = new Account();
            account.setCurrentBalance(new BigDecimal(100.0));
            account.setCustomerId(customerList.get(0).getCustomerId());
            account.setOpenDate(new Date());
            accountList.add(account);

        }
        return accountList;
    }

    public List<Account> createMockAccountListForTransaction(int count) throws PhoneNumberException, IdException, NullParameterException {
        List<Account> accountList = createMockAccountList(count);
        for (Account account: accountList) {
            account.setAccountId(new AccountId(IdGenerator.generateAccountId()));
            accountService.addOrUpdateAccount(account);
        }
        return  accountList;
    }

    private HttpPost createHttpPost(URI uri,String entityString) throws UnsupportedEncodingException {
        HttpPost request = new HttpPost();
        request.setURI(uri);
        fillHttpRequest(request,entityString);
        return request;

    }
    private HttpPut createHttpPut(URI uri,String entityString) throws UnsupportedEncodingException{
        HttpPut request = new HttpPut();
        request.setURI(uri);
        fillHttpRequest(request,entityString);
        return request;
    }
    public HttpResponse executeHttpRequestBase(String uriStr, String methodName) throws IOException, URISyntaxException {
        HttpRequestBase httpRequestBase = null;
        if(methodName.equals(HttpDelete.METHOD_NAME))
            httpRequestBase = new HttpDelete();
        else if(methodName.equals(HttpGet.METHOD_NAME))
            httpRequestBase = new HttpGet();
        if(httpRequestBase == null)
            return null;
        URI uri = builder.setPath(uriStr).build();
        httpRequestBase.setURI(uri);
        httpRequestBase.setHeader("Content-type", "application/json");
        return client.execute(httpRequestBase);
    }


    public HttpResponse executeHttpEntityEnclosingRequestBase(String uriStr, String entityString, String methodName) throws IOException, URISyntaxException {
        HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = null;
        URI uri = builder.setPath(uriStr).build();
        if(methodName.equals(HttpPost.METHOD_NAME))
            httpEntityEnclosingRequestBase = createHttpPost(uri,entityString);
        else if(methodName.equals(HttpPut.METHOD_NAME))
            httpEntityEnclosingRequestBase = createHttpPut(uri,entityString);
        if(httpEntityEnclosingRequestBase == null)
            return null;
        return client.execute(httpEntityEnclosingRequestBase);
    }



    private void fillHttpRequest(HttpEntityEnclosingRequestBase request, String entityString) throws UnsupportedEncodingException {
        if(entityString != null) {
            StringEntity entity = new StringEntity(entityString);
            request.setEntity(entity);
        }
        request.setHeader("Content-type", "application/json");
    }

    private static void init() throws NullParameterException {
        accountService = AccountServiceImpl.getAccountServiceInstance();
        transactionService = TransactionServiceImpl.getTransactionServiceInstance();
        customerService = CustomerServiceImpl.getCustomerServiceInstance();

        transactionService.setAccountService(accountService);
        accountService.setCustomerService(customerService);
    }

    private static void startServer() throws Exception {
        if (server == null) {
            server = new Server(8084);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
            servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                    AccountWebService.class.getCanonicalName()+","+TransactionWebService.class.getCanonicalName()+","+
                            CustomerWebService.class.getCanonicalName());
            server.start();
        }
    }
}
