package com.java.spring.books.controller;

import com.java.spring.books.exception.BaseException;
import com.java.spring.books.exception.ExceptionResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleExceptionController {
    @ExceptionHandler(BaseException.class)
    public ExceptionResponse handleException(BaseException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorCode(ex.getErrorCode());
        exceptionResponse.setStatus(ex.getStatus());
        return exceptionResponse;
    }
}
