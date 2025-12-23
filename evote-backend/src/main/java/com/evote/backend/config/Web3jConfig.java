package com.evote.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;


@Configuration
public class Web3jConfig {
    @Value("${WEB3J_CLIENT_ADDRESS}")
    private String rpcUrl;


    // Gas config (legacy gas model is fine for Anvil/most dev nets)
    @Value("${GAS_PRICE_GWEI}")
    private long gasPriceGwei;

    @Value("${GAS_LIMIT}")
    private long gasLimit;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(rpcUrl));
    }

    @Bean
    public BigInteger chainId(Web3j web3j) {
        try {
            return web3j.ethChainId().send().getChainId();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to fetch chainId from RPC: " + rpcUrl, e);
        }
    }

    //todo: adjust gas provider to use priority fee if needed
    @Bean
    public ContractGasProvider contractGasProvider() {
        // Convert configured gas price from GWEI to WEI
        BigInteger gasPrice = Convert.toWei(String.valueOf(gasPriceGwei), Convert.Unit.GWEI).toBigInteger();
        return new StaticGasProvider(gasPrice, BigInteger.valueOf(gasLimit));
    }

    @Bean
    public Credentials credentials(@Value("${PRIVATE_KEY}") String privateKey) {
        return Credentials.create(privateKey);
    }


//    @Bean
//    public TransactionManager transactionManager(Web3j web3j, Credentials deployerCredentials, BigInteger chainId) {
//        // FastRawTransactionManager signs locally and handles nonce; chainId ensures EIP-155 signing domain
//        return new FastRawTransactionManager(web3j, deployerCredentials, chainId.longValue());
//    }

    @Bean
    public TransactionManager transactionManager(Web3j web3j, Credentials credentials, BigInteger chainId) {
        // We use a PollingTransactionReceiptProcessor to wait for the receipt
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(web3j, 1000, 40);

        return new FastRawTransactionManager(
                web3j,
                credentials,
                chainId.longValue(),
                receiptProcessor
        );
    }
}