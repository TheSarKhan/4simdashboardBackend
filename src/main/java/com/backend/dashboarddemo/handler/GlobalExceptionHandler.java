package com.backend.dashboarddemo.handler;

import com.backend.dashboarddemo.handler.exception.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException dataNotFoundException) {
        ErrorResponse errorResponse = new ErrorResponse(dataNotFoundException.getMessage(), "There isn't any data like that");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailCanNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmailCanNotFoundException(EmailCanNotFoundException emailCanNotFoundException) {
        ErrorResponse errorResponse = new ErrorResponse(emailCanNotFoundException.getMessage(), "Email can not found exception");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException userAlreadyExistException) {
        ErrorResponse errorResponse = new ErrorResponse(userAlreadyExistException.getMessage(), "User already exist exception");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIsInActiveException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserIsInActiveException userIsInActiveException) {
        ErrorResponse errorResponse = new ErrorResponse(userIsInActiveException.getMessage(), "User is in active exception");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(WrongPasswordException wrongPasswordException) {
        ErrorResponse errorResponse = new ErrorResponse(wrongPasswordException.getMessage(), "Wrong password exception");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), "Internal server error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException exception) {
        log.error("NoSuchElementException: {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), "Not found error");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage()).append("; ");
        }
        ErrorResponse errorResponse = new ErrorResponse(errorMessage.toString(), "Invalid request body");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Validation error: " + ex.getMessage(), "Invalid request parameter");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getParameterName(), "Parameter is required");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
