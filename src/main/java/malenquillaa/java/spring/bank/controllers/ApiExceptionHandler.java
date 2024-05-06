package malenquillaa.java.spring.bank.controllers;

import malenquillaa.java.spring.bank.models.payloads.responses.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({
            RuntimeException.class,
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public MessageResponse handleRuntimeException(Exception e) {
        return new MessageResponse(400, "Error: " + e.getMessage());
    }

    @ExceptionHandler({
            AuthenticationException.class,
            MissingServletRequestParameterException.class,
            UsernameNotFoundException.class,
            MissingRequestHeaderException.class
    })
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public MessageResponse handleAuthenticationException(Exception e) {
        return new MessageResponse(401, "Error: " + e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public MessageResponse handleBadCredentialsException() {
        return new MessageResponse(401, "Error: Username or password is incorrect");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public MessageResponse handleAccessDeniedException(AccessDeniedException e) {
        return new MessageResponse(403, "Error: " + e.getMessage());
    }
}
