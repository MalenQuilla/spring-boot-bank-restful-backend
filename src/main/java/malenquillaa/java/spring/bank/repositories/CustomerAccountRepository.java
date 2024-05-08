package malenquillaa.java.spring.bank.repositories;

import malenquillaa.java.spring.bank.models.CustomerAccount;
import malenquillaa.java.spring.bank.models.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {

    @Query("select ca from CustomerAccount ca where ca.user.status = :status")
    List<CustomerAccount> findAllByUserStatus(EStatus status);

    Optional<CustomerAccount> findCustomerAccountByUserId(Long id);

    Optional<CustomerAccount> findCustomerAccountByAccountNumberAndUserStatus(String accountNumber, EStatus status);

    @Modifying
    @Query("update CustomerAccount ca set ca.balance = :balance where ca = :customerAccount")
    void updateCustomerAccountBalance(CustomerAccount customerAccount, Long balance);

    @Modifying
    @Query("update CustomerAccount ca set ca.lastTransactionTime = :lastTransactionTime where ca = :customerAccount")
    void updateLastTransactionTimeByCustomerAccount(CustomerAccount customerAccount, Date lastTransactionTime);
}
