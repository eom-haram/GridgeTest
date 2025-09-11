package com.tnovel.demo.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvisor {
    @ExceptionHandler(CustomException.class)
    public ProblemDetail handle(CustomException e) {
        log.warn(e.getMessage(), e);
        return this.build(e.getExceptionType().getStatus(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handle(MethodArgumentNotValidException e) {
        String message = stringify(e);
        log.warn(message, e);
        return this.build(HttpStatus.NOT_FOUND, message);
    }

    private String stringify(MethodArgumentNotValidException e) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMessageBuilder.append(fieldError.getField()).append(": ");
            errorMessageBuilder.append(fieldError.getDefaultMessage()).append(", ");
        }
        errorMessageBuilder.deleteCharAt(errorMessageBuilder.length() - 2);

        return errorMessageBuilder.toString();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handle(ConstraintViolationException e) {
        String message = stringify(e);
        log.warn(message, e);
        return this.build(HttpStatus.BAD_REQUEST, message);
    }

    private String stringify(ConstraintViolationException e) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ConstraintViolation fieldError : e.getConstraintViolations()) {
            stringBuilder.append(fieldError.getPropertyPath()).append(": ");
            stringBuilder.append(fieldError.getMessage()).append(", ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 2);

        return stringBuilder.toString();
    }

    private ProblemDetail build(HttpStatus status, String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setTitle(status.getReasonPhrase());
        return problemDetail;
    }
}
