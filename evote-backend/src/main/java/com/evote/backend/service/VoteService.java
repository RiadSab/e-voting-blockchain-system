package com.evote.backend.service;

import com.evote.backend.contract.CommitmentRegistry;
import com.evote.backend.dto.VoteRequest;
import com.evote.backend.exception.BlockchainException;
import com.evote.backend.exception.InvalidVoteException;
import com.evote.backend.exception.VoteAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Numeric;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Bytes32;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class VoteService {
    private final Web3j web3j;

    private final String contractAddress;

    private final List<Credentials> credentials;
    private final AtomicInteger voterIndex = new AtomicInteger(8);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(500_000);
    private static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L); // 20 Gwei

    Logger log = LoggerFactory.getLogger(VoteService.class);

    public VoteService(Web3j web3j, @Qualifier("deployedContractAddress") String contractAddress, List<Credentials> credentials) {
        this.web3j = web3j;
        this.contractAddress = contractAddress;
        this.credentials = credentials;
    }


    public String castVote(VoteRequest voteRequest) {
        String commitmentHash = voteRequest.getCommitment();
        //double check (even though the validation should have been done at controller level)
        if(commitmentHash == null || !commitmentHash.startsWith("0x")) {
            throw new InvalidVoteException("Commitment hash is invalid");
        }
        log.info("castVote commitmentHash {}", commitmentHash);

        Credentials currentVoter = credentials.get(voterIndex.getAndIncrement() % credentials.size());

        try {
            // load the contract wrapper
            CommitmentRegistry registry = CommitmentRegistry.load(
                    contractAddress,
                    web3j,
                    currentVoter,
                    new StaticGasProvider(GAS_PRICE, GAS_LIMIT)
            );

            //convert commitmentHash to byte32
            byte[] rawBytes = Numeric.hexStringToByteArray(commitmentHash);
            byte[] fixedBytes = new byte[32];
            System.arraycopy(rawBytes, 0, fixedBytes, 0, Math.min(rawBytes.length, 32));

            // SIMULATE THE CALL
            Function function = new Function(
                    "submitCommitment", // Function name in Solidity
                    List.of(new Bytes32(fixedBytes)), // Input parameters
                    Collections.emptyList() // Output parameters (void)
            );

            String encodedFunction = FunctionEncoder.encode(function);

            // Ask the node: "What happens if I send this?"
            EthCall ethCall = web3j.ethCall(
                    Transaction.createEthCallTransaction(currentVoter.getAddress(), contractAddress, encodedFunction),
                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST
            ).send();

            // 3. Check results
            if (ethCall.isReverted()) {
                String revertReason = ethCall.getRevertReason();
                if (revertReason != null && revertReason.contains("Vote already committed")) {
                    log.warn("Voter committed duplicated vote");
                    throw new VoteAlreadyExistsException("Vote with this commitment already exists");
                }
                // This prints the REAL reason (e.g., "Vote already committed")
                log.error("💥 Contract Revert Reason: {}", ethCall.getRevertReason());
                throw new BlockchainException("Contract Reverted: " + ethCall.getRevertReason());
            }

            log.info("Simulation Passed. Sending Real Transaction...");


            // call the smart contract method to register the commitment
            TransactionReceipt receipt = registry.submitCommitment(fixedBytes).send();

            // return the hash of the transaction
            return receipt.getTransactionHash();
        } catch (VoteAlreadyExistsException e) {
            throw e; // let it free to be handled by the global exception handler
        } catch (Exception e) {
            throw new BlockchainException("Failed to cast vote", e);
        }
    }

}
