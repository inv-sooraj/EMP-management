package com.project.employee.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

	public UserNotFoundException() {
        super(NOT_FOUND);
    }

    public UserNotFoundException(String reason) {
        super(NOT_FOUND, reason);
    }

    public UserNotFoundException(String reason, Throwable cause) {
        super(NOT_FOUND, reason, cause);
    }

}
