package com.evote.backend.service;


import static com.evote.backend.mapper.VoteParamMapper.toBigInt;
import static com.evote.backend.mapper.VoteParamMapper.toBigIntList;
import com.evote.backend.contract.Election;
import com.evote.backend.dto.SubmitVoteRequest;
import com.evote.backend.dto.SubmitVoteResponse;
import com.evote.backend.dto.VoteStatus;
import com.evote.backend.entitiy.records.TxResult;
import com.evote.backend.factory.ContractLoader;
import com.evote.backend.repository.ElectionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class VoteService {

    private final BlockchainTransactionService txService;
    private final ContractLoader contractLoader;
    private final ElectionRepository electionRepo;

    Logger log = LoggerFactory.getLogger(VoteService.class);

    public SubmitVoteResponse submitVote(SubmitVoteRequest req, UUID electionId){

        String electionContractAddress = electionRepo.findAddressById(electionId)
                .orElseThrow(() -> new IllegalArgumentException("Election not found"));


        Election electionContract = contractLoader.loadElectionContract(electionContractAddress);

        BigInteger c1x = toBigInt(req.getC1x(), "c1x");
        BigInteger c1y = toBigInt(req.getC1y(), "c1y");
        BigInteger c2x = toBigInt(req.getC2x(), "c2x");
        BigInteger c2y = toBigInt(req.getC2y(), "c2y");

        // SemaphoreProof signature:
        // SemaphoreProof(BigInteger merkleTreeDepth, BigInteger merkleTreeRoot,
        //                BigInteger nullifier, BigInteger message, BigInteger scope,
        //                List<BigInteger> points)

        var reqProof = req.getSemaphoreProof();

        BigInteger merkleTreeDepth = toBigInt(reqProof.getMerkleTreeDepth(), "merkleTreeDepth");
        BigInteger merkleTreeRoot = toBigInt(reqProof.getMerkleRoot(), "merkleTreeRoot");
        BigInteger nullifier = toBigInt(reqProof.getNullifier(), "nullifier");
        BigInteger message = toBigInt(reqProof.getMessage(), "message");
        BigInteger scope = toBigInt(reqProof.getScope(), "scope");

        List<BigInteger> points = toBigIntList(reqProof.getProof(), "proof");

        if (points.size() != 8) {
            throw new IllegalArgumentException("proof must have exactly 8 elements, got " + points.size());
        }

        Election.SemaphoreProof semaphoreProof = new Election.SemaphoreProof(
                merkleTreeDepth, merkleTreeRoot, nullifier, message, scope, points
        );

        TxResult txResult = txService.sendAndWait("submitVote",
                () -> electionContract.submitVote(c1x, c1y, c2x, c2y, semaphoreProof)
        );

        log.info("Vote submitted in tx {}", txResult.txHash());

        return new SubmitVoteResponse(
                txResult.txHash(),
                VoteStatus.SUCCESS,
                Instant.now().getEpochSecond(),
                txResult.receipt().getBlockNumber(),
                "Vote submitted successfully"
        );
    }
}
