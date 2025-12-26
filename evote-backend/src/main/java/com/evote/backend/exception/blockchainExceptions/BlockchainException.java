package com.evote.backend.exception.blockchainExceptions;


//TODO: This the parent exception for blockchain related issues, will be extended by later
public class BlockchainException extends RuntimeException{
    public BlockchainException(String message) {
        super(message);
    }

    public BlockchainException(String message, Throwable cause) {
        super(message, cause);
    }
}
