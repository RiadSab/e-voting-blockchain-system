package com.evote.backend.entitiy.records;

import java.math.BigInteger;

public record ContractTx(
        String to,
        String data,
        BigInteger value,
        BigInteger gasPrice,
        BigInteger gasLimit
) {}