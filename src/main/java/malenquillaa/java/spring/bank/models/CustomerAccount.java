package malenquillaa.java.spring.bank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "account_number")
        })
public class CustomerAccount {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String accountNumber;

    private Long balance = 50000L;

    private String currency = "VND";

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    public CustomerAccount(User user, String accountNumber) {
        this.user = user;
        this.accountNumber = accountNumber;
    }
}
