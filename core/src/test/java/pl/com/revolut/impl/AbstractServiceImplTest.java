package pl.com.revolut.impl;

import pl.com.revolut.common.utils.impl.IdGenerator;
import pl.com.revolut.common.utils.impl.ServiceUtils;
import pl.com.revolut.exception.IdException;
import pl.com.revolut.exception.NullParameterException;
import pl.com.revolut.exception.PhoneNumberException;
import pl.com.revolut.model.Account;
import pl.com.revolut.model.Customer;
import pl.com.revolut.model.PhoneNumber;
import pl.com.revolut.model.Transaction;
import pl.com.revolut.model.identifier.AccountId;
import pl.com.revolut.model.identifier.CustomerId;
import pl.com.revolut.model.identifier.TransactionId;
import pl.com.revolut.service.AccountService;
import pl.com.revolut.service.CustomerService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AbstractServiceImplTest {
    public List<Transaction> transactionList = new ArrayList<>();
    public List<Account> accountList = new ArrayList<>();
    public static List<Customer> customerList = new ArrayList<>();

    protected List<Account> createMockAccount(int count) throws NullParameterException, IdException, PhoneNumberException {
        createMockCustomer(1);

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

    protected List<Transaction> createMockTransaction(int count) throws NullParameterException, IdException, PhoneNumberException {
        ServiceUtils.checkParameters(count);
        createMockAccount(2);

        for (int i = 0 ;i< count;i ++) {
            Transaction transaction = new Transaction();
            String accIdStr = IdGenerator.generateTransactionId();
            transaction.setTransactionId(new TransactionId(accIdStr));
            transaction.setSenderAccountId(accountList.get(0).getAccountId());
            transaction.setReceiverAccountId(accountList.get(1).getAccountId());
            transaction.setDate(new Date());
            transaction.setAmount(new BigDecimal(100.0));
            transaction.setExplanation("EXPLANATION"+count);

            transactionList.add(transaction);
        }

        return transactionList;
    }

    protected List<Customer> createMockCustomer(int count) throws PhoneNumberException, NullParameterException, IdException{
        if(count > 499)
            count = 499;
        for (int i = 0 ;i< count;i ++) {
            Customer customer = new Customer();
            customer.setCustomerName("burak"+ count);
            customer.setCustomerSurname("cabuk"+count);
            customer.setCustomerPhone(new PhoneNumber(530+count, 250+count, 400+count));
            customer.setCustomerId(new CustomerId(IdGenerator.generateCustomerId()));
            customer.setAttendDate(Calendar.getInstance().getTime());
            customerList.add(customer);
        }

        return customerList;

    }
}
