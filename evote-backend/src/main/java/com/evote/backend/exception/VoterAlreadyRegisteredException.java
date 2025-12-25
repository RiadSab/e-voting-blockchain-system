package com.evote.backend.exception;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.UUID;

@Getter @Setter
public class VoterAlreadyRegisteredException extends RuntimeException {
    private final UUID electionId;
    private final BigInteger identityCommitment; // optional to expose
    private final String txHash;                 // optional
    private final String correlationId;          // optional

    public VoterAlreadyRegisteredException(
            UUID electionId,
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