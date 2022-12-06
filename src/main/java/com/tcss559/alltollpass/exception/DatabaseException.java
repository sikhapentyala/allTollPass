package com.tcss559.alltollpass.exception;

/**
 * @author sikha
 * DB Exception
 *
 */

public class DatabaseException extends RuntimeException{

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }
}
