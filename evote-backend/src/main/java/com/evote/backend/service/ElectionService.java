package com.evote.backend.service;

import static com.evote.backend.mapper.VoteParamMapper.toBigInt;
import com.evote.backend.contract.Election;
import com.evote.backend.contract.Semaphore;
import com.evote.backend.dto.*;
import com.evote.backend.entitiy.records.TxResult;
import com.evote.backend.exception.BlockchainTxException;
import com.evote.backend.exception.VoterAlreadyRegisteredException;
import com.evote.backend.factory.ContractLoader;
import com.evote.backend.repository.ElectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ElectionService {

    private final BlockchainViewService viewService;
    private final BlockchainTransactionService txService;
    private final ContractLoader contractLoader;
    private final ElectionRepository electionRepo;


    public SemaphoreInputsDto getSemaphoreInputs(UUID electionId)  {

        String electionAddress = electionRepo.findAddressById(electionId)
                .orElseThrow(() -> new IllegalArgumentException("Election not found"));

        Election election = contractLoader.loadElectionContract(electionAddress);

        String semaphoreAddress = viewService.callView("semaphoreAddress", election::semaphore);

        String groupId = viewService.callView("semaphoreGroupId", election::semaphoreGroupId).toString();

        Semaphore semaphore = contractLoader.loadSemaphoreContract(semaphoreAddress);

        String merkleTreeDepth = viewService.callView("getMerkleTreeDepth", () ->
                semaphore.getMerkleTreeDepth(new BigInteger(groupId))).toString();

        String merkleTreeRoot = viewService.callView("getMerkleTreeRoot", () ->
                semaphore.getMerkleTreeRoot(new BigInteger(groupId))).toString();


        return new SemaphoreInputsDto(
                semaphoreAddress,
                groupId,
                merkleTreeDepth,
                merkleTreeRoot
        );
    }


    public VoterRegistrationResponse registerVoter(UUID electionId, VoterRegistrationRequest request) {
        String identityCommitment = request.getIdentityCommitment();
        String electionContractAddress = electionRepo.findAddressById(electionId)
                .orElseThrow(() -> new IllegalArgumentException("Election not found: " + electionId));

        Election electionContract = contractLoader.loadElectionContract(electionContractAddress);

        BigInteger identityCommitmentBigInt = toBigInt(identityCommitment, "identityCommitment");

        TxResult txResult = null;
        try {
            txResult = txService.sendAndWait(
                    "registerVoter",
                    () -> electionContract.registerVoter(identityCommitmentBigInt)
            );
            return new VoterRegistrationResponse(
                    RegistrationStatus.SUCCESS,
                    "Voter registered successfully",
                    txResult.txHash()
            );

        } catch (BlockchainTxException e) {

            String revertReason = e.getRevertReason() == null ? "" : e.getRevertReason().toLowerCase();
            if(revertReason.contains("already registered")) {
                throw new VoterAlreadyRegisteredException(
                        electionId,
                        identityCommitmentBigInt,
                        e.getTxHash(),
                        e.getCorrelationId(),
                        e
                );
            }
            throw e;
        }
    }
}