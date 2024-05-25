package com.foodles.auth.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handle(BadCredentialsException ex){
        return new ResponseEntity<>("The username or password is incorrect", HttpStatus.valueOf(401));
    }

    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<String> handle(AccountStatusException ex){
        return new ResponseEntity<>("The account is locked", HttpStatus.valueOf(403));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handle(AccessDeniedException ex){
        return new ResponseEntity<>("You are not authorized to access this resource", HttpStatus.valueOf(403));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> handle(SignatureException ex){
        return new ResponseEntity<>("The JWT signature is invalid", HttpStatus.valueOf(403));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handle(ExpiredJwtException ex){
        return new ResponseEntity<>("The JWT token has expired", HttpStatus.valueOf(403));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handle(UsernameNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.valueOf(401));
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<String> handle(RefreshTokenException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.valueOf(403));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.valueOf(500));
    }
}
