package malenquillaa.java.spring.bank.models.payloads.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {

    @NotNull
    private String transactionType;

    @NotNull
    @Min(value = 500, message = "Minimum amount to transact is 500VND")
    private Long transactionAmount;
}
