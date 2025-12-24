package com.evote.backend.exception;

import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

@Getter @Setter
public class BlockchainTxException extends RuntimeException {
  private final String operation;
  private final String senderKey;
  private final String correlationId;
  private final String txHash;
  private final String revertReason;

  public BlockchainTxException(
          String operation,
          String senderKey,
          String correlationId,
          String txHash,
          String revertReason,
          Throwable cause
  ) {
    super(buildMessage(operation, senderKey, correlationId, txHash, revertReason, cause), cause);
    this.operation = operation;
    this.senderKey = senderKey;
    this.correlationId = correlationId;
    this.txHash = txHash;
    this.revertReason = revertReason;
  }

  private static String buildMessage(
          String operation,
          String senderKey,
          String correlationId,
          String txHash,
          String revertReason,
          Throwable cause
  ) {
    return getString(operation, senderKey, correlationId, txHash, revertReason, cause);
  }

  @NonNull
  public static String getString(String operation, String senderKey, String correlationId, String txHash, String revertReason, Throwable cause) {
    String base = "Blockchain tx failed op=%s senderKey=%s corr=%s".formatted(operation, senderKey, correlationId);
    if (txHash != null) base += " txHash=" + txHash;
    if (revertReason != null) base += " revertReason=" + revertReason;
    if (cause != null && cause.getMessage() != null) base += " cause=" + cause.getMessage();
    return base;
  }
}