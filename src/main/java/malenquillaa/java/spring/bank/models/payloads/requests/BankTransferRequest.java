package malenquillaa.java.spring.bank.models.payloads.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankTransferRequest {

    @NotNull
    @Min(value = 500, message = "Minimum amount to transact is 500VND")
    private Long transactionAmount;

    @NotNull
    @Size(min = 15, max = 15)
    private String targetAccountNumber;
}
