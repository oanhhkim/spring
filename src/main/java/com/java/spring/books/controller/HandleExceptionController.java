package com.java.spring.books.controller;

import com.java.spring.books.exception.BaseException;
import com.java.spring.books.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleExceptionController {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleException(BaseException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorCode(ex.getErrorCode());
        exceptionResponse.setStatus(ex.getStatus());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.valueOf(ex.getErrorCode()));
    }
}
