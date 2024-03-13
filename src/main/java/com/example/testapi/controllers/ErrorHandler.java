package com.example.testapi.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import com.example.testapi.exceptions.CustomException;
import com.example.testapi.exceptions.ResourceNotFoundException;
import com.example.testapi.models.Error;
import com.example.testapi.models.ResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private String getLocalDateTime() {
        return LocalDateTime.now(ZoneId.of("America/Chicago")).format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss a"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseWrapper<List<Error>> handleValidationException(MethodArgumentNotValidException ex) {
        List<Error> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.add(new Error(getLocalDateTime(), error.getDefaultMessage()));
        });

        log.error(ex.getMessage());
        return new ResponseWrapper<>(errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleCustomException(CustomException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseWrapper<>(new Error(getLocalDateTime(), e.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseWrapper<>(new Error(getLocalDateTime(), e.getMessage())), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleAuthenticationException(AuthenticationException e ){
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseWrapper<>(new Error(getLocalDateTime(), e.getMessage())), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());

        return new ResponseEntity<>(new ResponseWrapper<>(new Error(getLocalDateTime(), e.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage());

        return new ResponseEntity<>(new ResponseWrapper<>(new Error(getLocalDateTime(), e.getMessage())), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleMultipartException(MultipartException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseWrapper<>(new Error(getLocalDateTime(), e.getMessage())), HttpStatus.BAD_REQUEST);
    }

}
