package com.tcss559.alltollpass.exception;

public class RfidNotFoundException extends Throwable {

    public RfidNotFoundException(String message) {
        super(message);
    }

    public RfidNotFoundException(Throwable cause) {
        super(cause);
    }
}
