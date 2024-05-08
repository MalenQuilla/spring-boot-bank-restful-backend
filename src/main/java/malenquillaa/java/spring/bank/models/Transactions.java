package malenquillaa.java.spring.bank.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date transactionDate;

    private String currency = "VND";

    private ETransaction transactionType;

    @Min(500)
    private Long transactionAmount;

    @Positive
    private Long openingBalance;

    @Positive
    private Long closingBalance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private CustomerAccount customer;

    public Transactions(Date transactionDate,
                        Long transactionAmount,
                        ETransaction transactionType,
                        Long openingBalance,
                        Long closingBalance,
                        CustomerAccount customer) {
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.openingBalance = openingBalance;
        this.closingBalance = closingBalance;
        this.customer = customer;
    }
}
