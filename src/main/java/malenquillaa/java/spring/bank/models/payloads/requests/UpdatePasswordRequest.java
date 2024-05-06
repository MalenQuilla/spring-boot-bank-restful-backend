package malenquillaa.java.spring.bank.models.payloads.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {
    @NotNull
    private String oldPassword;

    @NotNull
    private String newPassword;
}
