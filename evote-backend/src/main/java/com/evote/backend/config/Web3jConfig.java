package com.evote.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Web3jConfig {
    @Value("${WEB3J_CLIENT_ADDRESS}")
    private String rpcUrl;


    @Value("${MNEMONIC}")
    private String mnemonic;
    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(rpcUrl));
    }

    @Bean
    public List<Credentials> credentials() {
        List<Credentials> credentials = new ArrayList<>();

        // generate the master keypair from the mnemonic
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);

        for(int i = 0; i < 50; i++) {
            // derive the keypair using the derivation path m/44'/60'/0'/0/i
            int[] derivationPath = {
                    44 | Bip32ECKeyPair.HARDENED_BIT,
                    60 | Bip32ECKeyPair.HARDENED_BIT,
                    Bip32ECKeyPair.HARDENED_BIT,
                    0,
                    i}; // index i for each account

            Bip32ECKeyPair derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, derivationPath);
            credentials.add(Credentials.create(derivedKeyPair));
        }
        return credentials;
    }
}