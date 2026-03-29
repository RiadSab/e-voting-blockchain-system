package com.evote.backend.vote.exception;

// Used for invalid vote scenarios: election closed, voter not registered, invalid candidate, etc.

public class InvalidVoteException extends RuntimeException{
    public InvalidVoteException(String message) {
        super(message);
    }
}