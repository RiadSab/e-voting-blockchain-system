package com.evote.backend.exception.blockchainExceptions;


public class BlockchainReadException extends RuntimeException {

    private final String operation;
    private final String correlationId;
    private final String contractAddress;

    public BlockchainReadException(
            String operation,
            String correlationId,
            String contractAddress,
            Throwable cause
    ) {
        super(buildMessage(operation, correlationId, contractAddress, cause), cause);
        this.operation = operation;
        this.correlationId = correlationId;
        this.contractAddress = contractAddress;
    }

    public String getOperation() {
        return operation;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    private static String buildMessage(
            String operation,
            String correlationId,
            String contractAddress,
            Throwable cause
    ) {
        return getString(operation, correlationId, contractAddress, cause);
    }

    public static String getString(
            String operation,
            String correlationId,
            String contractAddress,
            Throwable cause
    ) {
        String base = "Blockchain read failed op=%s corr=%s".formatted(operation, correlationId);
        if (contractAddress != null) base += " contract=" + contractAddress;
        if (cause != null && cause.getMessage() != null) base += " cause=" + cause.getMessage();
        return base;
    }

}
