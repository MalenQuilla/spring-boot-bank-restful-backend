package malenquillaa.java.spring.bank.repositories;

import malenquillaa.java.spring.bank.models.EStatus;
import malenquillaa.java.spring.bank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByStatus(EStatus status);

    Optional<User> findByUsernameAndStatus(String username, EStatus status);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Modifying
    @Query("update User u set u.password = :password where u.username = :username")
    void updatePasswordByUsername(String username, String password);

    @Modifying
    @Query("update User u set u.email = :email where u.username = :username")
    void updateEmailByUsername(String username, String email);

    @Modifying
    @Query("update User u set u.status = :status where u.id = :id")
    void updateStatusById(Long id, EStatus status);
}
