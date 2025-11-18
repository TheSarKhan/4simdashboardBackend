package com.backend.dashboarddemo.handler.exception;

public class UserIsInActiveException extends RuntimeException{
    public UserIsInActiveException(String message) {
        super(message);
    }
}
