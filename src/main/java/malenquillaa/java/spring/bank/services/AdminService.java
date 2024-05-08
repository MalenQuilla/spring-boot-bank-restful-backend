package malenquillaa.java.spring.bank.services;

import malenquillaa.java.spring.bank.models.EStatus;
import malenquillaa.java.spring.bank.models.User;
import malenquillaa.java.spring.bank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AdminService {

    UserRepository userRepository;

    @Autowired
    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
