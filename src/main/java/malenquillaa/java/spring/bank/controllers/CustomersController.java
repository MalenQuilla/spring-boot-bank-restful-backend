package malenquillaa.java.spring.bank.controllers;

import io.swagger.v3.oas.annotations.tags.Tags;
import malenquillaa.java.spring.bank.models.payloads.requests.BankTransferRequest;
import malenquillaa.java.spring.bank.models.payloads.requests.TransactionRequest;
import malenquillaa.java.spring.bank.services.CustomersService;
import malenquillaa.java.spring.bank.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/transaction/auth")
    public ResponseEntity<?> getTimestampToken(String password) {
        return ResponseEntity.ok(transactionsService.getTimestampToken(password));
    }

    @PatchMapping("/transaction/self")
    public ResponseEntity<?> makeTransaction(@RequestHeader(name = "timestampToken") String timestampToken,
                                             TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionsService.makeSelfTransaction(timestampToken, transactionRequest));
    }

    @GetMapping("/transaction/other/name")
    public ResponseEntity<?> getTargetName(String accountNumber) {
        return ResponseEntity.ok(transactionsService.getUsernameByAccountNumber(accountNumber));
    }

    @PatchMapping("/transaction/other")
    public ResponseEntity<?> makeBankTransfer(@RequestHeader(name = "timestampToken") String timestampToken,
                                              BankTransferRequest bankTransferRequest) {
        return ResponseEntity.ok(transactionsService.makeBankTransfer(timestampToken, bankTransferRequest));
    }
}
