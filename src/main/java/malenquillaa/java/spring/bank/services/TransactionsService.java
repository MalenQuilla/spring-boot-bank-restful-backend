package malenquillaa.java.spring.bank.services;

import malenquillaa.java.spring.bank.models.CustomerAccount;
import malenquillaa.java.spring.bank.models.ETransaction;
import malenquillaa.java.spring.bank.models.Transactions;
import malenquillaa.java.spring.bank.models.payloads.requests.TransactionRequest;
import malenquillaa.java.spring.bank.models.payloads.responses.TransactionResponse;
import malenquillaa.java.spring.bank.repositories.CustomerAccountRepository;
import malenquillaa.java.spring.bank.repositories.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TransactionsService {

    CustomerAccountRepository customerAccountRepository;
    TransactionsRepository transactionsRepository;
    CustomersService customersService;

    @Autowired
    public TransactionsService(TransactionsRepository transactionsRepository, CustomersService customersService, CustomerAccountRepository customerAccountRepository) {
        this.transactionsRepository = transactionsRepository;
        this.customersService = customersService;
        this.customerAccountRepository = customerAccountRepository;
    }

    public TransactionResponse makeTransaction(TransactionRequest transactionRequest) {
        CustomerAccount customerAccount = customersService.getCustomerAccount();

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

        return new TransactionResponse(
                transactionDate,
                openingBalance,
                transactions.getClosingBalance()
        );
    }
}
