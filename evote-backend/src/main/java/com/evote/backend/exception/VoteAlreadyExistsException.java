package com.evote.backend.exception;

public class VoteAlreadyExistsException extends RuntimeException {
    public VoteAlreadyExistsException(String message) {
        super(message);
    }
}
