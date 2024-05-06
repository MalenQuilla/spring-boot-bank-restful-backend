package malenquillaa.java.spring.bank.models.payloads.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
	@NotNull
	private String username;

	@NotNull
	private String password;
}
