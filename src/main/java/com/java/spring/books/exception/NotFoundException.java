package com.java.spring.books.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(long id) {
        setErrorCode(404);
        setStatus(HttpStatus.valueOf(404));
    }
}
