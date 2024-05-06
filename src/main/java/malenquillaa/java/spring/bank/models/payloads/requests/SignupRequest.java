package malenquillaa.java.spring.bank.models.payloads.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    @NotNull
    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20")
    private String username;

    @NotNull
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> roles;

    @NotNull
    @Size(min = 6, max = 40, message = "Password length must be between 6 and 40")
    private String password;
}
