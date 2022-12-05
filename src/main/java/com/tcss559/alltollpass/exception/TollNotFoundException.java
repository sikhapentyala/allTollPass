package com.tcss559.alltollpass.exception;

public class TollNotFoundException extends RuntimeException{
    public TollNotFoundException(String message) {
        super(message);
    }

    public TollNotFoundException(Throwable cause) {
        super(cause);
    }
}
