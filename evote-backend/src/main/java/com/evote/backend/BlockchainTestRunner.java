package com.evote.backend;

import com.evote.backend.contract.CommitmentRegistry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.crypto.Credentials;
import org.web3j.tx.gas.DefaultGasProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class BlockchainTestRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(BlockchainTestRunner.class);
    private final Web3j web3j;

    @Value("${wallet.private-key}")
    private String privateKey;

    private final String contractAddress;

    public BlockchainTestRunner(Web3j web3j, @Qualifier("deployedContractAddress") String contractAddress) {
        this.web3j = web3j;
        this.contractAddress = contractAddress;
    }

    @Override
    public void run(@NotNull String... args) throws Exception {
        log.info("Connecting to Ganache");

        // 1. Check Network Version
        String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
        log.info("Connected to Network: {}", clientVersion);

        // 2. Load Wallet
        Credentials credentials = Credentials.create(privateKey);
        log.info("Wallet Loaded: {}", credentials.getAddress());

        // 3. Load Contract
        CommitmentRegistry registry = CommitmentRegistry.load(
                contractAddress,
                web3j,
                credentials,
                new DefaultGasProvider()
        );

        // 4. Validate Contract
        if (registry.isValid()) {
            log.info("Contract Connection Successful!");
            log.info("Address: {}", contractAddress);
        } else {
            log.info("Error: Contract not found at {}", contractAddress);
        }
    }
}