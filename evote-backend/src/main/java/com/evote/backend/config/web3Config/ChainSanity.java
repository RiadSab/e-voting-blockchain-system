package com.evote.backend.config.web3Config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigInteger;

@AllArgsConstructor
@Service
public class ChainSanity {
    private final BigInteger rpcChain;
    private final DeploymentsInfo deploymentsInfo;



    // Sanity check to ensure that the chain ID from the RPC matches the chain ID in the deployments info
    @PostConstruct
    public void checkChainConsistency() {
        BigInteger deployedChainId = deploymentsInfo.getChainId();
        if (!rpcChain.equals(deployedChainId)) {
            throw new IllegalStateException(String.format(
                    "Chain ID mismatch: RPC chain ID is %s but deployments info chain ID is %s",
                    rpcChain, deployedChainId));
        }
        // we can add more checks here in the future if needed, like checking network name, etc.
    }
}
