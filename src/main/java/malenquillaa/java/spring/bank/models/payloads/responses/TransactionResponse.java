package malenquillaa.java.spring.bank.models.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class TransactionResponse {
    private String fromAccountNumber;
    private String fromName;
    private String toAccountNumber;
    private String toName;
    private Date date;
    private Long openingBalance;
    private Long closingBalance;
}
