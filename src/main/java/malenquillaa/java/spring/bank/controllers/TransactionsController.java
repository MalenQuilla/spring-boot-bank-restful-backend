package malenquillaa.java.spring.bank.controllers;

import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Tags
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TransactionsController {
}
