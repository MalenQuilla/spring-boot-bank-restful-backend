package malenquillaa.java.spring.bank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "account_number")
        })
public class CustomerAccount {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String accountNumber;

    @Min(0)
    @NotNull
    private Long balance = 50000L;

    @NotNull
    private String currency = "VND";

    @JsonIgnore
    private Date lastTransactionTime = null;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    public CustomerAccount(User user, String accountNumber) {
        this.user = user;
        this.accountNumber = accountNumber;
    }
}
