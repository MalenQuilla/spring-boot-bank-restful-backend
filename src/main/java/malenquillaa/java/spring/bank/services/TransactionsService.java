package malenquillaa.java.spring.bank.services;

import malenquillaa.java.spring.bank.models.*;
import malenquillaa.java.spring.bank.models.payloads.requests.BankTransferRequest;
import malenquillaa.java.spring.bank.models.payloads.requests.TransactionRequest;
import malenquillaa.java.spring.bank.models.payloads.responses.TransactionResponse;
import malenquillaa.java.spring.bank.repositories.CustomerAccountRepository;
import malenquillaa.java.spring.bank.repositories.TransactionsRepository;
import malenquillaa.java.spring.bank.services.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TransactionsService {

    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    CustomerAccountRepository customerAccountRepository;
    TransactionsRepository transactionsRepository;
    CustomersService customersService;

    @Autowired
    public TransactionsService(TransactionsRepository transactionsRepository,
                               CustomersService customersService,
                               CustomerAccountRepository customerAccountRepository,
                               JwtUtils jwtUtils,
                               PasswordEncoder passwordEncoder) {
        this.transactionsRepository = transactionsRepository;
        this.customersService = customersService;
        this.customerAccountRepository = customerAccountRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public String getTimestampToken(String password) {
        CustomerAccount customerAccount = customersService.getCustomerAccount();
        String customerPassword = customerAccount.getUser().getPassword();

        if (!passwordEncoder.matches(password, customerPassword))
            throw new AuthenticationException("Password is incorrect") {};

        return jwtUtils.generateTimestampToken();
    }

    public TransactionResponse makeSelfTransaction(String timestampToken, TransactionRequest transactionRequest) {
        CustomerAccount customerAccount = customersService.getCustomerAccount();

        if (!validateTimeStampToken(customerAccount, timestampToken))
            throw new AuthenticationException("Invalid timestamp token") {};

        Transactions transactions = makeTransaction(customerAccount, transactionRequest);

        String customerName = getUsernameByCustomerAccount(customerAccount);

        return new TransactionResponse(
                customerAccount.getAccountNumber(),
                customerName,
                customerAccount.getAccountNumber(),
                customerName,
                transactions.getTransactionDate(),
                transactions.getOpeningBalance(),
                transactions.getClosingBalance()
        );
    }

    public TransactionResponse makeBankTransfer(String timestampToken, BankTransferRequest bankTransferRequest) {
        CustomerAccount currentCustomerAccount = customersService.getCustomerAccount();

        if (!validateTimeStampToken(currentCustomerAccount, timestampToken))
            throw new AuthenticationException("Invalid timestamp token") {};
        if (currentCustomerAccount.getAccountNumber().equals(bankTransferRequest.getTargetAccountNumber()))
            throw new RuntimeException("Cannot make bank transfer to your self account");

        CustomerAccount otherCustomerAccount = customerAccountRepository
                .findCustomerAccountByAccountNumberAndUserStatus(bankTransferRequest.getTargetAccountNumber(), EStatus.STATUS_ACTIVE)
                .orElseThrow(() -> new RuntimeException("Target account not found"));

        Long transactionAmount = bankTransferRequest.getTransactionAmount();

        Transactions currentTransaction = makeTransaction(currentCustomerAccount,
                new TransactionRequest("DEBIT", transactionAmount));
        Transactions otherTransaction = makeTransaction(otherCustomerAccount,
                new TransactionRequest("CREDIT", transactionAmount));

        String currentCustomerName = getUsernameByCustomerAccount(currentCustomerAccount);
        String otherCustomerName = getUsernameByCustomerAccount(otherCustomerAccount);

        return new TransactionResponse(
                currentCustomerAccount.getAccountNumber(),
                currentCustomerName,
                otherCustomerAccount.getAccountNumber(),
                otherCustomerName,
                currentTransaction.getTransactionDate(),
                currentTransaction.getOpeningBalance(),
                currentTransaction.getClosingBalance()
        );
    }

    public String getUsernameByAccountNumber(String accountNumber) {
        CustomerAccount customerAccount = customerAccountRepository
                .findCustomerAccountByAccountNumberAndUserStatus(accountNumber, EStatus.STATUS_ACTIVE)
                .orElseThrow(() -> new RuntimeException("Target account not found"));
        return getUsernameByCustomerAccount(customerAccount);
    }

    private boolean validateTimeStampToken(CustomerAccount customerAccount, String timestampToken) {
        if (jwtUtils.validateJwtToken(timestampToken)) {
            Date timestamp = jwtUtils.getIssuedDateFromToken(timestampToken);
            Date lastTimeStamp = customerAccount.getLastTransactionTime();

            return lastTimeStamp == null || timestamp.after(lastTimeStamp);
        }
        return false;
    }

    private String getUsernameByCustomerAccount(CustomerAccount customerAccount) {
        return customerAccount.getUser().getFirstName() + " " + customerAccount.getUser().getLastName();
    }

    private Transactions makeTransaction(CustomerAccount customerAccount, TransactionRequest transactionRequest) {
        Long openingBalance = customerAccount.getBalance();
        Long transactionAmount = transactionRequest.getTransactionAmount();
        Date transactionDate = new Date();

        Transactions transactions = switch (transactionRequest.getTransactionType()) {
            case "DEPOSIT", "CREDIT" -> new Transactions(
                    transactionDate,
                    transactionAmount,
                    ETransaction.TYPE_CREDIT,
                    openingBalance,
                    openingBalance + transactionAmount,
                    customerAccount);
            case "WITHDRAW", "DEBIT" ->  new Transactions(
                    transactionDate,
                    transactionAmount,
                    ETransaction.TYPE_DEBIT,
                    openingBalance,
                    openingBalance - transactionAmount,
                    customerAccount);
            default -> throw new IllegalArgumentException("Unknown transaction type");
        };

        if(transactions.getClosingBalance() < 0)
            throw new RuntimeException("Not enough balance to make transaction");

        customerAccountRepository.updateCustomerAccountBalance(customerAccount, transactions.getClosingBalance());
        transactionsRepository.save(transactions);
        customerAccountRepository.updateLastTransactionTimeByCustomerAccount(customerAccount, transactionDate);

        return transactions;
    }
}
