package com.evote.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class Web3jConfig {
    @Value("${WEB3J_CLIENT_ADDRESS}")
    private String rpcUrl;

    @Value("${MNEMONIC}")
    private String mnemonic;

    @Value("${ACCOUNT_COUNT}")
    private int accountCount;

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

    @Bean
    public ContractGasProvider contractGasProvider() {
        // Convert configured gas price from GWEI to WEI
        BigInteger gasPrice = Convert.toWei(String.valueOf(gasPriceGwei), Convert.Unit.GWEI).toBigInteger();
        return new StaticGasProvider(gasPrice, BigInteger.valueOf(gasLimit));
    }

    @Bean
    public List<Credentials> credentialsList() {
        // Derive accounts from mnemonic using path m/44'/60'/0'/0/i (matches Anvil/MetaMask)
        List<Credentials> creds = new ArrayList<>(accountCount);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        Bip32ECKeyPair master = Bip32ECKeyPair.generateKeyPair(seed);

        for (int i = 0; i < accountCount; i++) {
            int[] path = new int[] {
                    44 | Bip32ECKeyPair.HARDENED_BIT,
                    60 | Bip32ECKeyPair.HARDENED_BIT,
                    Bip32ECKeyPair.HARDENED_BIT,
                    0,
                    i
            };
            Bip32ECKeyPair derived = Bip32ECKeyPair.deriveKeyPair(master, path);
            creds.add(Credentials.create(derived));
        }
        return creds;
    }

    // Primary deployer/operator (index 0). Change index if you need another account.
    @Bean
    public Credentials deployerCredentials(List<Credentials> credentialsList) {
        if (credentialsList.isEmpty()) {
            throw new IllegalStateException("No credentials derived from mnemonic");
        }
        return credentialsList.get(0);
    }

    @Bean
    public TransactionManager transactionManager(Web3j web3j, Credentials deployerCredentials, BigInteger chainId) {
        // FastRawTransactionManager signs locally and handles nonce; chainId ensures EIP-155 signing domain
        return new FastRawTransactionManager(web3j, deployerCredentials, chainId.longValue());
    }
}