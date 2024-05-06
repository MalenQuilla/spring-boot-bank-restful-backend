package malenquillaa.java.spring.bank.controllers;

import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import malenquillaa.java.spring.bank.models.payloads.requests.LoginRequest;
import malenquillaa.java.spring.bank.models.payloads.requests.SignupRequest;
import malenquillaa.java.spring.bank.models.payloads.requests.UpdatePasswordRequest;
import malenquillaa.java.spring.bank.models.payloads.responses.JwtResponse;
import malenquillaa.java.spring.bank.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = accountService.login(loginRequest);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        accountService.createAccount(signupRequest);

        return ResponseEntity.ok("User registered successfully");
    }

    @PatchMapping("/update/password")
    public ResponseEntity<?> updatePassword(UpdatePasswordRequest updateRequest) {
        accountService.updatePassword(updateRequest);

        return ResponseEntity.ok("Password updated successfully");
    }

    @PatchMapping("/update/email")
    public ResponseEntity<?> updateEmail(@Email String newEmail) {
        accountService.updateEmail(newEmail);

        return ResponseEntity.ok("Email updated successfully");
    }
}
