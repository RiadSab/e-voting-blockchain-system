package com.evote.backend;

import com.evote.backend.contract.CommitmentRegistry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.crypto.Credentials;
import org.web3j.tx.gas.DefaultGasProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class BlockchainTestRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(BlockchainTestRunner.class);

    private final Web3j web3j;

    private final String contractAddress;

    private final List<Credentials> credentials;

    private final AtomicInteger voterIndex = new AtomicInteger(8);
    public BlockchainTestRunner(Web3j web3j, @Qualifier("deployedContractAddress") String contractAddress, List<Credentials> credentials) {
        this.web3j = web3j;
        this.contractAddress = contractAddress;
        this.credentials = credentials;
    }

    @Override
    public void run(@NotNull String... args) throws Exception {
        log.info("Connecting to Ganache");

        // 1. Check Network Version
        String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
        log.info("Connected to Network: {}", clientVersion);

        // 2. Select Voter Credentials
        Credentials currentVoter = credentials.get(voterIndex.getAndIncrement() % credentials.size());
        // Load Wallet
        log.info("Wallet Loaded: {}", currentVoter.getAddress());

        // Load Contract
        CommitmentRegistry registry = CommitmentRegistry.load(
                contractAddress,
                web3j,
                currentVoter,
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