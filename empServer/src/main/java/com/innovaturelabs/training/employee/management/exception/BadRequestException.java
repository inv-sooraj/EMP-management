
package com.innovaturelabs.training.employee.management.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.server.ResponseStatusException;


public class BadRequestException extends ResponseStatusException {

    public BadRequestException() {
        super(BAD_REQUEST);
    }

    public BadRequestException(String reason) {
        super(BAD_REQUEST, reason);
    }

    public BadRequestException(String reason, Throwable cause) {
        super(BAD_REQUEST, reason, cause);
    }
}
