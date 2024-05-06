package malenquillaa.java.spring.bank.repositories;

import malenquillaa.java.spring.bank.models.ERole;
import malenquillaa.java.spring.bank.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
