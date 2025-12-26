package com.evote.backend.factory;

import com.evote.backend.config.web3Config.DeploymentsInfo;
import com.evote.backend.contract.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

@Service
@RequiredArgsConstructor
public class ContractLoader {
    private final Web3j web3j;
    private final TransactionManager txManager;
    private final ContractGasProvider gasProvider;
    private final DeploymentsInfo deploymentsInfo;


    public Election loadElectionContract(String contractAddress) {
        return Election.load(contractAddress, web3j, txManager, gasProvider);
    }

    public ElectionFactory loadElectionFactoryContract() {
        String contractAddress = deploymentsInfo.getFactory();
        return ElectionFactory.load(contractAddress, web3j, txManager, gasProvider);
    }

    public TallyVerifier loadTallyVerifierContract() {
        String contractAddress = String.valueOf(deploymentsInfo.getTallyVerifier());
        return TallyVerifier.load(contractAddress, web3j, txManager, gasProvider);
    }

    public Semaphore loadSemaphoreContract() {
        String contractAddress = deploymentsInfo.getSemaphoreAddress();
        return Semaphore.load(contractAddress, web3j, txManager, gasProvider);
    }

    public Semaphore loadSemaphoreContract(String contractAddress) {
        return Semaphore.load(contractAddress, web3j, txManager, gasProvider);
    }
}