package com.evote.backend.service;


import com.evote.backend.contract.Election;
import com.evote.backend.contract.Semaphore;
import com.evote.backend.dto.SemaphoreInputsDto;
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
}