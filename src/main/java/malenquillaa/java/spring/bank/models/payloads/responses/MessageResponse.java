package malenquillaa.java.spring.bank.models.payloads.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MessageResponse {
    private int code;
    private String message;
}