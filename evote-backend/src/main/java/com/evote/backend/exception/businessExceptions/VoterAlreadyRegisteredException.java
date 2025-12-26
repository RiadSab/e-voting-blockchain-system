package com.evote.backend.exception.businessExceptions;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
public class VoterAlreadyRegisteredException extends RuntimeException {
    private final String electionId;
    private final BigInteger identityCommitment; // optional to expose
    private final String txHash;                 // optional
    private final String correlationId;          // optional

    public VoterAlreadyRegisteredException(
            String electionId,
            BigInteger identityCommitment,
            String txHash,
            String correlationId,
            Throwable cause
    ) {
        super("Voter already registered for election " + electionId, cause);
        this.electionId = electionId;
        this.identityCommitment = identityCommitment;
        this.txHash = txHash;
        this.correlationId = correlationId;
    }
}