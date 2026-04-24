package com.evote.backend.voter.service;

import com.evote.backend.blockchain.exception.BlockchainTxException;
import com.evote.backend.blockchain.factory.ContractLoader;
import com.evote.backend.blockchain.service.BlockchainTransactionService;
import com.evote.backend.blockchain.tx.TxResult;
import com.evote.backend.election.entity.Election;
import com.evote.backend.election.repository.ElectionRepository;
import com.evote.backend.shared.enums.RegistrationStatus;
import com.evote.backend.voter.dto.VoterRegistrationRequest;
import com.evote.backend.voter.dto.VoterRegistrationResponse;
import com.evote.backend.voter.exception.VoterAlreadyRegisteredException;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.UUID;

import static com.evote.backend.shared.mapper.SharedMapper.toBigInt;

@Service
@RequiredArgsConstructor
public class VoterService {

    private final BlockchainTransactionService txService;
    private  final ElectionRepository electionRepo;
    private final ContractLoader contractLoader;

    public VoterRegistrationResponse registerVoter(UUID electionId, VoterRegistrationRequest request) {
        String identityCommitment = request.getIdentityCommitment();

        Election electionEntity = electionRepo.findById(electionId)
                .orElseThrow(() -> new IllegalArgumentException("Election not found: " + electionId));
        String electionContractAddress = electionEntity.getContractAddress();



        com.evote.backend.blockchain.contract.Election electionContract = contractLoader.loadElectionContract(electionContractAddress);

        BigInteger identityCommitmentBigInt = toBigInt(identityCommitment, "identityCommitment");

        TxResult txResult = null;
        try {
            txResult = txService.sendAndWait(
                    "registerVoter",
                    () -> electionContract.registerVoter(identityCommitmentBigInt)
            );

            // Update election entity with registration block numbers

            Boolean firstBlockFetched = electionEntity.isFirstBlockFetched();
            if(!firstBlockFetched){
                electionEntity.setFirstRegistrationBlockNumber(txResult.receipt().getBlockNumber().toString());
                electionEntity.setFirstBlockFetched(true);
            }
            electionEntity.setLastRegistrationBlockNumber(txResult.receipt().getBlockNumber().toString());
            electionRepo.save(electionEntity);
            return new VoterRegistrationResponse(
                    RegistrationStatus.SUCCESS,
                    "Voter registered successfully",
                    txResult.txHash()
            );

        } catch (BlockchainTxException e) {

            String revertReason = e.getRevertReason() == null ? "" : e.getRevertReason().toLowerCase();
            if(revertReason.contains("already registered")) {
                throw new VoterAlreadyRegisteredException(
                        electionId.toString(),
                        identityCommitmentBigInt,
                        e.getTxHash(),
                        e.getCorrelationId() != null ? e.getCorrelationId() : MDC.get("correlationId"),
                        e
                );
            }
            throw e;
        }
    }



}
