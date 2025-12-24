package com.evote.backend.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class DeploymentsInfo {
    private final BigInteger chainId;
    private final String network;
    private final String factory;
    private final String tallyVerifier;
    private final String semaphoreAddress;

    public Optional<String> getTallyVerifier() {
        return (tallyVerifier == null || tallyVerifier.equalsIgnoreCase("0x0000000000000000000000000000000000000000"))
                ? Optional.empty()
                : Optional.of(tallyVerifier);
    }
}
