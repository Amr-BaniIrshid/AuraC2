package com.server.contestControl.authServer.exception.api;

import com.server.contestControl.authServer.exception.api.base.ApiException;
import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends ApiException {
    public DuplicateEmailException(String email) {
        super("The email '" + email + "' is already registered.", HttpStatus.CONFLICT);
    }
}
