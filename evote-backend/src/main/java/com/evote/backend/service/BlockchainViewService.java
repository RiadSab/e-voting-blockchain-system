package com.evote.backend.service;

import com.evote.backend.exception.blockchainExceptions.BlockchainReadException;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.RemoteFunctionCall;

import java.util.UUID;
import java.util.function.Supplier;

@Service
public class BlockchainViewService {
    // For view calls that do not modify blockchain state
    public <T> T callView(String operation, Supplier<RemoteFunctionCall<T>> callSupplier) {
        try {
            RemoteFunctionCall<T> call = callSupplier.get();
            if (call == null) throw new IllegalStateException("callSupplier returned null");
            return call.send();
        } catch (Exception e) {
            throw new BlockchainReadException(
                    operation,
                    UUID.randomUUID().toString(),
                    null, // TODO: decide if we want to pass contract address here
                    e.getCause()
            );
        }
    }
}