package com.tcss559.alltollpass.exception;
/**
 * @author sikha
 *  Exceptions raised by extrenal services
 *
 */

public class ExtrenalServiceException extends RuntimeException{

    public ExtrenalServiceException(String message) {
        super(message);
    }

    public ExtrenalServiceException(Throwable cause) {
        super(cause);
    }
}


