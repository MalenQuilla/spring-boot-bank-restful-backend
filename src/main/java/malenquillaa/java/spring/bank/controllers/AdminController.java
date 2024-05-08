package malenquillaa.java.spring.bank.controllers;

import io.swagger.v3.oas.annotations.tags.Tags;
import malenquillaa.java.spring.bank.models.EStatus;
import malenquillaa.java.spring.bank.services.AdminService;
import malenquillaa.java.spring.bank.services.CustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tags
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    AdminService adminService;
    CustomersService customersService;

    @Autowired
    public AdminController(AdminService adminService,
                           CustomersService customersService) {
        this.adminService = adminService;
        this.customersService = customersService;
    }

    @GetMapping("/list/all")
    public ResponseEntity<?> getAllAccounts() {
        return ResponseEntity.ok(adminService.listAllUser());
    }

    @GetMapping("/list/active")
    public ResponseEntity<?> getAllActiveAccounts() {
        return ResponseEntity.ok(adminService.listAllUserByStatus(EStatus.STATUS_ACTIVE));
    }

    @GetMapping("/list/deleted")
    public ResponseEntity<?> getAllDeletedAccounts() {
        return ResponseEntity.ok(adminService.listAllUserByStatus(EStatus.STATUS_DELETED));
    }

    @GetMapping("/list/restricted")
    public ResponseEntity<?> getAllRestrictedAccounts() {
        return ResponseEntity.ok(adminService.listAllUserByStatus(EStatus.STATUS_RESTRICTED));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOtherAccount(@PathVariable Long id) {
        adminService.setStatusById(id, EStatus.STATUS_DELETED);

        return ResponseEntity.ok("Account deleted successfully");
    }

    @PatchMapping("/active/{id}")
    public ResponseEntity<?> activeAccount(@PathVariable Long id) {
        adminService.setStatusById(id, EStatus.STATUS_ACTIVE);
        return ResponseEntity.ok("Account activated successfully");
    }

    @DeleteMapping("/restrict/{id}")
    public ResponseEntity<?> restrictAccount(@PathVariable Long id) {
        adminService.setStatusById(id, EStatus.STATUS_RESTRICTED);
        return ResponseEntity.ok("Account restricted successfully");
    }

    @GetMapping("/list/customers/all")
    public ResponseEntity<?> getAllCustomerAccounts() {
        return ResponseEntity.ok(customersService.listAllCustomerAccounts());
    }

    @GetMapping("/list/customers/active")
    public ResponseEntity<?> getAllActiveCustomers() {
        return ResponseEntity.ok(customersService.listAllCustomerAccountsByUserStatus(EStatus.STATUS_ACTIVE));
    }

    @GetMapping("/list/customers/deleted")
    public ResponseEntity<?> getAllDeletedCustomers() {
        return ResponseEntity.ok(customersService.listAllCustomerAccountsByUserStatus(EStatus.STATUS_DELETED));
    }

    @GetMapping("/list/customers/restricted")
    public ResponseEntity<?> getAllRestrictedCustomers() {
        return ResponseEntity.ok(customersService.listAllCustomerAccountsByUserStatus(EStatus.STATUS_RESTRICTED));
    }
}
