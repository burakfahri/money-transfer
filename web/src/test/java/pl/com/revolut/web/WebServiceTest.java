package pl.com.revolut.web;

import com.google.gson.Gson;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.PhoneNumber;
import pl.com.revolut.model.identifier.CustomerId;
import pl.com.revolut.service.AccountService;
import pl.com.revolut.service.CustomerService;
import pl.com.revolut.service.TransactionService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
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
        server.stop();
        HttpClientUtils.closeQuietly(client);
    }

    public static List<Customer> createMockCustomer(int count) throws PhoneNumberException, NullParameterException, IdException {
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

    public HttpResponse executeHttpCommand(URI uri,String entityString,String methodName) throws IOException {
        HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = null;
        if(methodName.equals(HttpPost.METHOD_NAME))
            httpEntityEnclosingRequestBase = createHttpPost(uri,entityString);
        else if(methodName.equals(HttpPut.METHOD_NAME))
            httpEntityEnclosingRequestBase = createHttpPut(uri,entityString);
        return client.execute(httpEntityEnclosingRequestBase);
    }

    private void fillHttpRequest(HttpEntityEnclosingRequestBase request, String entityString) throws UnsupportedEncodingException {
        StringEntity entity = new StringEntity(entityString);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
    }

    private static void init() {
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
