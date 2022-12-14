package com.tcss559.alltollpass.handler;

import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.RfidNotFoundException;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * @author sikha
 * Handling exceptions -
 *
 */

@RestControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(value
            = { DatabaseException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected Error handleDatabase(
            RuntimeException ex, WebRequest request) {
        return Error.builder().message(ex.getLocalizedMessage()).build();
    }

    @ExceptionHandler(value
            = { UserNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Error handleUserNotFound(RuntimeException ex, WebRequest request) {
        return Error.builder().message(ex.getLocalizedMessage()).build();
    }

    @ExceptionHandler(value
            = { RfidNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Error handleRfidNotFound(
            RuntimeException ex, WebRequest request) {
        return Error.builder().message(ex.getLocalizedMessage()).build();
    }
}
