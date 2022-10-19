package com.example.scrapper.exception.throwables;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class PriceSampleServiceException extends Exception {
    @Getter
    private HttpStatus status;
    public PriceSampleServiceException(String message) {
        super(message);
    }
    public PriceSampleServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
