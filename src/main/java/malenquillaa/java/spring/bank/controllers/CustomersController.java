package malenquillaa.java.spring.bank.controllers;

import io.swagger.v3.oas.annotations.tags.Tags;
import malenquillaa.java.spring.bank.models.payloads.requests.TransactionRequest;
import malenquillaa.java.spring.bank.services.CustomersService;
import malenquillaa.java.spring.bank.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tags
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/customer")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomersController {

    CustomersService customersService;
    TransactionsService transactionsService;

    @Autowired
    public CustomersController(CustomersService customersService, TransactionsService transactionsService) {
        this.customersService = customersService;
        this.transactionsService = transactionsService;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {
        return ResponseEntity.ok(customersService.getCustomerAccount());
    }

    @GetMapping("/transact")
    public ResponseEntity<?> makeTransaction(TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionsService.makeTransaction(transactionRequest));
    }
}
