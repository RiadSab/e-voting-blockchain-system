package com.evote.backend.entity.txUtilities;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

public record TxResult(
        boolean success,
        String operation,
        String correlationId,
        String txHash,
        TransactionReceipt receipt
) {
    public static TxResult success(String operation, String correlationId, TransactionReceipt receipt) {
        return new TxResult(true, operation, correlationId, receipt.getTransactionHash(), receipt);
    }
}