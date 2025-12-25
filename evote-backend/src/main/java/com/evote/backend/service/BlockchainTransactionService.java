package com.evote.backend.service;

import com.evote.backend.entitiy.records.RetryPolicy;
import com.evote.backend.entitiy.records.TxResult;
import com.evote.backend.exception.BlockchainTxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.ClientConnectionException;
import org.web3j.protocol.exceptions.TransactionException;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Service

public class BlockchainTransactionService {

    private static final Logger log = LoggerFactory.getLogger(BlockchainTransactionService.class);

    private static final String senderKey = "relayer";

    private final ReentrantLock nonceLock = new ReentrantLock(true);

    // We were using a default retry policy, now we are using the Bean injected
    private final RetryPolicy retryPolicy;

    public BlockchainTransactionService(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }



    public TxResult sendAndWait(
            String operation,
            Supplier<RemoteFunctionCall<TransactionReceipt>> txCallSupplier
    ) {
        Objects.requireNonNull(operation, "operation");
        Objects.requireNonNull(txCallSupplier, "txCallSupplier");

        String correlationId = UUID.randomUUID().toString();

        nonceLock.lock();
        try {
            return sendWithRetries(operation, correlationId, txCallSupplier);
        } finally {
            nonceLock.unlock();
        }
    }

    private TxResult sendWithRetries(
            String operation,
            String correlationId,
            Supplier<RemoteFunctionCall<TransactionReceipt>> txCallSupplier
    ) {
        int attempt = 0;
        Duration backoff = retryPolicy.initialBackoff();

        while (true) {
            attempt++;
            try {
                TransactionReceipt receipt = doSend(txCallSupplier);

                if(receipt.getStatus() != null && !"0x1".equals(receipt.getStatus())){
                    String txHash = receipt.getTransactionHash();
                    String revertReason = receipt.getRevertReason();
                    throw new BlockchainTxException(operation, senderKey,correlationId, txHash, revertReason, null);
                }
                logSuccess(operation, attempt, receipt, correlationId);
                return TxResult.success(operation, correlationId, receipt);

            } catch (TransactionException e) {
                throw mapTransactionException(operation, correlationId, attempt, e);

            } catch (ClientConnectionException e) {
                if (!canRetry(attempt)) throw mapRpcFailure(operation, correlationId, attempt, e);
                backoff = retrySleepAndNext(operation, attempt, backoff, correlationId, e);

            } catch (RuntimeException e) {
                if (!shouldRetryMempoolOrNonce(e, attempt)) throw mapUnexpected(operation, correlationId, attempt, e);
                backoff = retrySleepAndNext(operation, attempt, backoff, correlationId, e);

            }catch (Exception e) { // checked exception path
                throw new BlockchainTxException(operation, senderKey, correlationId, null, null, e);
            }
        }
    }

    private TransactionReceipt doSend(Supplier<RemoteFunctionCall<TransactionReceipt>> txCallSupplier) throws Exception {
        RemoteFunctionCall<TransactionReceipt> txCall = txCallSupplier.get();
        if (txCall == null) throw new IllegalStateException("txCallSupplier returned null");
        return txCall.send();
    }

    private boolean canRetry(int attempt) {
        return attempt < retryPolicy.maxAttempts();
    }

    private Duration retrySleepAndNext(String operation, int attempt, Duration backoff, String correlationId, Exception e) {
        log.info("tx retry op={} attempt={} backoffMs={} corr={} msg={}",
                operation, attempt, backoff.toMillis(), correlationId, e.getMessage());
        sleep(backoff);
        return nextBackoff(backoff);
    }

    private void logSuccess(String operation, int attempt, TransactionReceipt receipt, String correlationId) {
        log.info("tx ok op={} attempt={} txHash={} corr={}",
                operation, attempt, receipt.getTransactionHash(), correlationId);
    }

    private BlockchainTxException mapRpcFailure(
            String operation,
            String correlationId,
            int attempt,
            ClientConnectionException e
    ) {
        log.error("RPC failure op={} attempt={} correlationId={}", operation, attempt, correlationId, e);
        return new BlockchainTxException(operation, senderKey, correlationId, null, null, e);
    }

    private BlockchainTxException mapTransactionException(
            String operation,
            String correlationId,
            int attempt,
            TransactionException e
    ) {
        String txHash = extractTxHash(e).orElse(null);

        String revertReason = extractRevertReason(e).orElseGet(() -> extractRevertReason2(e)); // your helper (best-effort)

        log.warn(
                "Tx failed op={} attempt={} correlationId={} txHash={} revertReason={}",
                operation, attempt, correlationId, txHash, revertReason, e
        );

        return new BlockchainTxException(operation, senderKey, correlationId, txHash, revertReason, e);
    }

    private BlockchainTxException mapUnexpected(String operation, String correlationId, int attempt, RuntimeException e) {
        log.error("tx unexpected failure op={} attempt={} corr={}", operation, attempt, correlationId, e);
        String revertReason = extractRevertReason2(e);
        return new BlockchainTxException(operation, senderKey, correlationId, null, revertReason, e);
    }

    private  Duration nextBackoff(Duration current) {
        long doubled = Math.max(1, current.toMillis()) * 2;
        return Duration.ofMillis(Math.min(doubled, retryPolicy.maxBackoff().toMillis()));
        // If you prefer: use retryPolicy.maxBackoff() but then method cannot be static.
    }

    private static void sleep(Duration d) {
        try {
            Thread.sleep(Math.max(0, d.toMillis()));
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting to retry tx", ie);
        }
    }

    private  boolean shouldRetryMempoolOrNonce(RuntimeException e, int attempt) {
        if (attempt >= retryPolicy.maxAttempts()) return false;
        String msg = (e.getMessage() == null) ? "" : e.getMessage().toLowerCase();
        return msg.contains("nonce too low")
                || msg.contains("replacement transaction underpriced")
                || msg.contains("already known")
                || msg.contains("known transaction")
                || msg.contains("temporarily unavailable")
                || msg.contains("timeout");
    }

    private static Optional<String> extractTxHash(TransactionException e) {
        if (e.getTransactionHash().isPresent()) return e.getTransactionHash();
        return e.getTransactionReceipt().map(TransactionReceipt::getTransactionHash);
    }

    private static Optional<String> extractRevertReason(TransactionException e) {
        return e.getTransactionReceipt().map(TransactionReceipt::getRevertReason);

    }

    private static String extractRevertReason2(Throwable t) {
        Throwable cur = t;
        while (cur != null) {
            String m = cur.getMessage();
            if (m != null && m.toLowerCase().contains("revert")) return m;
            cur = cur.getCause();
        }
        return null;
    }


}

