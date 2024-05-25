package com.foodles.auth.exception;

public class RefreshTokenException extends RuntimeException{

    public RefreshTokenException(String message) {
        super(message);
    }
}
