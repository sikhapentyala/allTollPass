package com.tcss559.alltollpass.exception;

/**
 * @author sikha
 * Specific Exception for RFID not found
 *
 */

public class RfidNotFoundException extends RuntimeException {

    public RfidNotFoundException(String message) {
        super(message);
    }

    public RfidNotFoundException(Throwable cause) {
        super(cause);
    }
}
