package com.evote.backend.entitiy.records;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.time.Instant;

public record TxRecord(
        String requestId,
        String idempotencyKey,
        String operation,
        Instant createdAt,
        TxStatus status,
        String txHash,
        TransactionReceipt receipt,
        String error
) {}