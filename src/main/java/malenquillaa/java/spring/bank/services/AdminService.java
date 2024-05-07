package malenquillaa.java.spring.bank.services;

import jakarta.transaction.Transactional;
import malenquillaa.java.spring.bank.models.CustomerAccount;
import malenquillaa.java.spring.bank.models.EStatus;
import malenquillaa.java.spring.bank.models.User;
import malenquillaa.java.spring.bank.repositories.CustomerAccountRepository;
import malenquillaa.java.spring.bank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(Transactional.TxType.REQUIRED)
public class AdminService {

    UserRepository userRepository;
    CustomerAccountRepository customerAccountRepository;

    @Autowired
    public AdminService(UserRepository userRepository,
                        CustomerAccountRepository customerAccountRepository) {
        this.userRepository = userRepository;
        this.customerAccountRepository = customerAccountRepository;
    }

    public List<User> listAllUser() {
        return userRepository.findAll();
    }

    public List<User> listAllUserByStatus(EStatus status) {
        return userRepository.findAllByStatus(status);
    }

    public void setStatusById(Long id, EStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isAdmin()) {
            throw new RuntimeException("Cannot change admin account status");
        }

        if (user.getStatus() == status) {
            throw new RuntimeException("Account status already set");
        }
        userRepository.updateStatusById(id, status);
    }

    public List<CustomerAccount> listAllCustomerAccountsByUserStatus(EStatus status) {
        return customerAccountRepository.findAllByUserStatus(status);
    }

    public List<CustomerAccount> listAllCustomerAccounts() {
        return customerAccountRepository.findAll();
    }
}
