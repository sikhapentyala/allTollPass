package com.tcss559.alltollpass.exception;

/**
 * @author sikha
 * Specific Exception for Toll not found
 *
 */

public class TollNotFoundException extends RuntimeException{
    public TollNotFoundException(String message) {
        super(message);
    }

    public TollNotFoundException(Throwable cause) {
        super(cause);
    }
}
