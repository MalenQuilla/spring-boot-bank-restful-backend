package malenquillaa.java.spring.bank.repositories;

import malenquillaa.java.spring.bank.models.CustomerAccount;
import malenquillaa.java.spring.bank.models.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {

    @Query("select ca from CustomerAccount ca where ca.user.status = :status")
    List<CustomerAccount> findAllByUserStatus(EStatus status);
}
