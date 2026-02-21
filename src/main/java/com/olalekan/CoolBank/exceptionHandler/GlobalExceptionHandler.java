package com.olalekan.CoolBank.exceptionHandler;

import com.olalekan.CoolBank.exception.DuplicateResourceException;
import com.olalekan.CoolBank.exception.DuplicateTransactionException;
import com.olalekan.CoolBank.exception.ExpiredTokenException;
import com.olalekan.CoolBank.exception.IncorrectAmountException;
import com.olalekan.CoolBank.exception.InsufficientBalanceException;
import com.olalekan.CoolBank.exception.InvalidUserStatusException;
import com.olalekan.CoolBank.exception.PaymentInitializationException;
import com.olalekan.CoolBank.exception.PaymentVerificationException;
import com.olalekan.CoolBank.exception.UnauthorizeUserException;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
                );

        return ApiResponse.builder()
                .error(true)
                .message("Invalid arguments")
                .data(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExpiredTokenException.class)
    public ApiResponse expiredTokenExceptionHandler(ExpiredTokenException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .message("Expired Token error")
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateResourceException.class)
    public ApiResponse duplicateResourceExceptionHandler(DuplicateResourceException ex){
        Map<String, String> error = new HashMap<>();

        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .message("duplicate resource error")
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse noSuchElementExceptionHandler(NoSuchElementException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .message("No such element error")
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ApiResponse usernameNotFoundExceptionHandler(UsernameNotFoundException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .message("Invalid User error")
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InsufficientBalanceException.class)
    public ApiResponse insufficientBalanceExceptionHandler(InsufficientBalanceException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .message("Insufficient balance error")
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateTransactionException.class)
    public ApiResponse duplicateTransactionExceptionHandler(DuplicateTransactionException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .message("duplicate transaction error")
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizeUserException.class)
    public ApiResponse unauthorizeUserExceptionHandler(UnauthorizeUserException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .message("Unauthorized User error")
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(PaymentInitializationException.class)
    public ApiResponse paymentInitializationExceptionHandler(PaymentInitializationException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .message("Payment initialization error")
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(PaymentVerificationException.class)
    public ApiResponse paymentVerificationExceptionHandler(PaymentVerificationException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .message("Payment Verification error")
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(IncorrectAmountException.class)
    public ApiResponse incorrectAmountExceptionHandler(IncorrectAmountException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .message("Incorrect amount error")
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidUserStatusException.class)
    public Map<String, String> invalidUserStatusExceptionHandler(InvalidUserStatusException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ApiResponse badCredentialsExceptionHandler(BadCredentialsException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .message("Bad Credentials error")
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ApiResponse illegalStateExceptionHandler(IllegalStateException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpClientErrorException.class)
    public ApiResponse httpClientErrorExceptionHandler(HttpClientErrorException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .message("Http Client error")
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpServerErrorException.class)
    public ApiResponse httpServerErrorExceptionHandler(HttpServerErrorException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse illegalArgumentExceptionHandler(IllegalArgumentException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .data(error)
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse exceptionHandler(Exception ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ApiResponse.builder()
                .error(true)
                .data(error)
                .build();
    }


}
