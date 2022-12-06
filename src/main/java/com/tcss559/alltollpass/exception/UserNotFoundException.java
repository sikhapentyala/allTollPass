package com.tcss559.alltollpass.exception;

/**
 * @author sikha
 * Specific Exception for User not found
 *
 */

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
}
