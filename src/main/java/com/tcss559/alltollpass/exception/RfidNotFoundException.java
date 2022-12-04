package com.tcss559.alltollpass.exception;

public class RfidNotFoundException extends RuntimeException {

    public RfidNotFoundException(String message) {
        super(message);
    }

    public RfidNotFoundException(Throwable cause) {
        super(cause);
    }
}
