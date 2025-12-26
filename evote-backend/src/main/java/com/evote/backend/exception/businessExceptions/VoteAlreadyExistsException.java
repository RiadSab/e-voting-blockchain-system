package com.evote.backend.exception.businessExceptions;

import lombok.Getter;

@Getter
public class VoteAlreadyExistsException extends RuntimeException {
    private final String electionId;
    private final String correlationId;
    private final String txHash;
    public VoteAlreadyExistsException(String message, String electionId, String correlationId, String txHash) {
        super(message);
        this.electionId = electionId;
        this.correlationId = correlationId;
        this.txHash = txHash;
    }
}
