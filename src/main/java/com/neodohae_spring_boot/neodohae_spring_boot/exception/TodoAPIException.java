package com.neodohae_spring_boot.neodohae_spring_boot.exception;

import org.springframework.http.HttpStatus;

public class TodoAPIException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public TodoAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
