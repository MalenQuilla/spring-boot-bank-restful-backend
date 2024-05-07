package malenquillaa.java.spring.bank.models.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class TransactionResponse {
    private Date date;
    private Long openingBalance;
    private Long closingBalance;
}
