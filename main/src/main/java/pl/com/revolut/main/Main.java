package pl.com.revolut.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import pl.com.revolut.impl.AccountServiceImpl;
import pl.com.revolut.impl.CustomerServiceImpl;
import pl.com.revolut.impl.TransactionServiceImpl;
import pl.com.revolut.service.AccountService;
import pl.com.revolut.service.CustomerService;
import pl.com.revolut.service.TransactionService;
import pl.com.revolut.web.AccountWebService;
import pl.com.revolut.web.CustomerWebService;
import pl.com.revolut.web.TransactionWebService;


public class Main {

    private static AccountService accountService = null;
    private static TransactionService transactionService = null;
    private static CustomerService customerService = null;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Main.class);


    public static void main(String[] args) throws Exception {
        init();
        log.info("PROGRAM HAS BEEN STARTED");
        startService();
        log.info("SERVICE HAS BEEN STARTED");
    }

    /**
     * initiates the services
     */
    private static void init() {
        accountService = AccountServiceImpl.getAccountServiceInstance();
        transactionService = TransactionServiceImpl.getTransactionServiceInstance();
        customerService = CustomerServiceImpl.getCustomerServiceInstance();

        transactionService.setAccountService(accountService);
        accountService.setCustomerService(customerService);
    }

    /**
     * start the embeded jetty service
     * @throws Exception
     */
    private static void startService() throws Exception {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                AccountWebService.class.getCanonicalName()+","+TransactionWebService.class.getCanonicalName()+","+
                        CustomerWebService.class.getCanonicalName());
        try {
            server.start();
            server.join();
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
        }finally {
            server.destroy();
        }
    }
}
