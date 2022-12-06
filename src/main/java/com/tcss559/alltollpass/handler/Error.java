package com.tcss559.alltollpass.handler;

import lombok.Builder;
import lombok.Data;

/**
 * @author sikha
 * Entity for what the error to throw consists of .
 * Simplicity assumed it is just a String
 *
 */

@Data
@Builder
public class Error {
    private String message;
}
