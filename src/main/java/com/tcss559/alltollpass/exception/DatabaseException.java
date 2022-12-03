package com.tcss559.alltollpass.exception;

public class DatabaseException extends Throwable{

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }
}
