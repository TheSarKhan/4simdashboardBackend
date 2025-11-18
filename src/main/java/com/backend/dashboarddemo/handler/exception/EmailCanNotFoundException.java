package com.backend.dashboarddemo.handler.exception;

public class EmailCanNotFoundException extends RuntimeException {
    public EmailCanNotFoundException(String message) {
        super(message);
    }
}
