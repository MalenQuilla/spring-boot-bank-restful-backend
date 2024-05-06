package malenquillaa.java.spring.bank.controllers;

import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import malenquillaa.java.spring.bank.models.EStatus;
import malenquillaa.java.spring.bank.models.payloads.requests.LoginRequest;
import malenquillaa.java.spring.bank.models.payloads.requests.SignupRequest;
import malenquillaa.java.spring.bank.models.payloads.requests.UpdatePasswordRequest;
import malenquillaa.java.spring.bank.models.payloads.responses.JwtResponse;
import malenquillaa.java.spring.bank.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tags
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = accountService.login(loginRequest);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        accountService.createAccount(signupRequest);

        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllAccounts() {
        return ResponseEntity.ok(accountService.listAll());
    }

    @GetMapping("/all/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllActiveAccounts() {
        return ResponseEntity.ok(accountService.listAllUserByStatus(EStatus.STATUS_ACTIVE));
    }

    @GetMapping("/all/deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllDeletedAccounts() {
        return ResponseEntity.ok(accountService.listAllUserByStatus(EStatus.STATUS_DELETED));
    }

    @GetMapping("all/restricted")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRestrictedAccounts() {
        return ResponseEntity.ok(accountService.listAllUserByStatus(EStatus.STATUS_RESTRICTED));
    }

    @PatchMapping("/update/password")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public ResponseEntity<?> updatePassword(UpdatePasswordRequest updateRequest) {
        accountService.updatePassword(updateRequest);

        return ResponseEntity.ok("Password updated successfully");
    }

    @PatchMapping("/update/email")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public ResponseEntity<?> updateEmail(String newEmail) {
        accountService.updateEmail(newEmail);

        return ResponseEntity.ok("Email updated successfully");
    }

    @DeleteMapping("/delete/current")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public ResponseEntity<?> deleteCurrentAccount() {
        accountService.setStatusDeletedById(null);

        return ResponseEntity.ok("Account deleted successfully");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteOtherAccount(@PathVariable Long id) {
        accountService.setStatusDeletedById(id);

        return ResponseEntity.ok("Account deleted successfully");
    }

    @PatchMapping("/active/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activeAccount(@PathVariable Long id) {
        accountService.setStatusById(id, EStatus.STATUS_ACTIVE);
        return ResponseEntity.ok("Account activated successfully");
    }

    @DeleteMapping("/restrict/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> restrictAccount(@PathVariable Long id) {
        accountService.setStatusById(id, EStatus.STATUS_RESTRICTED);
        return ResponseEntity.ok("Account restricted successfully");
    }
}
