package malenquillaa.java.spring.bank.services;

import malenquillaa.java.spring.bank.models.CustomerAccount;
import malenquillaa.java.spring.bank.models.EStatus;
import malenquillaa.java.spring.bank.repositories.CustomerAccountRepository;
import malenquillaa.java.spring.bank.services.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomersService {

    CustomerAccountRepository customersRepository;

    @Autowired
    public CustomersService(CustomerAccountRepository customersRepository) {
        this.customersRepository = customersRepository;
    }

    public CustomerAccount getCustomerAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        return customersRepository.findCustomerAccountByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer account not found"));
    }

    public List<CustomerAccount> listAllCustomerAccountsByUserStatus(EStatus status) {
        return customersRepository.findAllByUserStatus(status);
    }

    public List<CustomerAccount> listAllCustomerAccounts() {
        return customersRepository.findAll();
    }
}
