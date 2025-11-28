package com.evote.backend.exception;

public class InvalidVoteException extends RuntimeException{
    public InvalidVoteException(String message) {
        super(message);
    }
}
