package com.evote.backend.election.service;

import com.evote.backend.blockchain.config.DeploymentsConfig;
import com.evote.backend.blockchain.contract.ElectionFactory;
import com.evote.backend.blockchain.contract.Semaphore;
import com.evote.backend.shared.enums.ElectionStatus;
import com.evote.backend.zk.merkle.dto.CreateSnapshotRequestDto;
import com.evote.backend.zk.semaphore.dto.SemaphoreInputsDto;
import com.evote.backend.election.dto.*;
import com.evote.backend.candidate.entity.Candidate;
import com.evote.backend.election.entity.Election;
import com.evote.backend.blockchain.tx.TxResult;
import com.evote.backend.blockchain.factory.ContractLoader;
import static  com.evote.backend.shared.mapper.SharedMapper.toBigInt;
import static com.evote.backend.election.mapper.ElectionMapper.toElectionDetailsDto;
import static com.evote.backend.election.mapper.ElectionMapper.toElectionEntity;
import static com.evote.backend.election.mapper.ElectionMapper.toElectionSummaryDto;

import com.evote.backend.candidate.repository.CandidateRepository;
import com.evote.backend.election.repository.ElectionRepository;
import com.evote.backend.blockchain.service.BlockchainTransactionService;
import com.evote.backend.blockchain.service.BlockchainViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ElectionService {

    private final BlockchainViewService viewService;
    private final BlockchainTransactionService txService;
    private final ContractLoader contractLoader;
    private final DeploymentsConfig deploymentsConfig;


    private final ElectionRepository electionRepo;
    private final CandidateRepository candidateRepository;


    public SemaphoreInputsDto getSemaphoreInputs(UUID electionId)  {

        String electionAddress = electionRepo.findContractAddressById(electionId)
                .orElseThrow(() -> new IllegalArgumentException("Election not found"));

        com.evote.backend.blockchain.contract.Election election = contractLoader.loadElectionContract(electionAddress);

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
                merkleTreeRoot, null, null, null // siblings, pathIndices, leafIndex to be filled by Merkle service
        );
    }


    public ElectionCreateResponse createElection(ElectionCreateRequest request) {
        String metadataCid = request.getIpfsCid();
        String metadataHash = request.getMetadataHash();
        Long startTime = request.getStartTime();
        Long endTime = request.getEndTime();
        String pky = request.getPky();
        String pkx = request.getPkx();
        String tallyVerifier = request.getTallyVerifier();

        // deploy the election contract, take the address, groupId
        ElectionFactory electionFactory = contractLoader.loadElectionFactoryContract();
        TxResult txResult = txService.sendAndWait(
                "createElection",
                () -> electionFactory.createElection(
                        metadataCid,
                        metadataHash.getBytes(),
                        toBigInt(pkx, "pkx"),
                        toBigInt(pky, "pky"),
                        toBigInt(startTime.toString(), "startTime"),
                        toBigInt(endTime.toString(), "endTime"),
                        tallyVerifier
                )
        );
        List<ElectionFactory.ElectionCreatedEventResponse> events = electionFactory.getElectionCreatedEvents(txResult.receipt());
        if (events.isEmpty()) {
            throw new IllegalStateException("No ElectionCreated event found in transaction " + txResult.txHash());
        }
        String electionAddress = events.get(0).electionAddress;

        com.evote.backend.blockchain.contract.Election election = contractLoader.loadElectionContract(electionAddress);
        BigInteger semaphoreGroupIdBigInt = viewService.callView("semaphoreGroupId", election::semaphoreGroupId);
        Long semaphoreGroupId = semaphoreGroupIdBigInt.longValue();
        BigInteger electionIdOnChain = events.get(0).electionId;


        // map the entity and save it in DB
        Election entity = toElectionEntity(request, semaphoreGroupId, electionAddress, electionIdOnChain);

        electionRepo.save(entity);
        return new ElectionCreateResponse(
                entity.getId().toString(),
                "Election created successfully, will be active at the start time",
                ElectionStatus.UPCOMING,
                txResult.txHash(),
                electionAddress,
                Long.toString(entity.getCreatedAt().getEpochSecond()),
                txResult.receipt().getBlockNumber().toString()
        );
    }

    public List<ElectionSummaryDto> getOpenElections() {
        List<Election> elections = electionRepo.findByClosedFalseOrderByCreatedAtDesc();
        return elections.stream()
                .map(e -> toElectionSummaryDto(e))
                .toList();
    }

    public ElectionDetailsDto getElectionDetails(UUID electionId) {
        Optional<Election> electionOpt = electionRepo.findById(electionId);
        if (electionOpt.isEmpty()) {
            throw new IllegalArgumentException("Election not found: " + electionId);
        }
        Election election = electionOpt.get();
        return toElectionDetailsDto(election);
    }

    //TODO: DECIDE WHAT TO RETURN EXACTLY later
    public UUID registerCandidate(UUID electionId, RegisterCandidate request) {
        Optional<Election> electionOpt = electionRepo.findById(electionId);
        if (electionOpt.isEmpty()) {
            throw new IllegalArgumentException("Election not found: " + electionId);
        }
        Candidate candidate = new Candidate();
        candidate.setElectionId(electionId);
        candidate.setFirstName(request.getFirstName());
        candidate.setLastName(request.getLastName());
        candidate.setPartyName(request.getPartyName());
        candidate.setPartyId(request.getPartyId());
        candidate.setManifestCid(request.getManifestCid());

        candidateRepository.save(candidate);
        return candidate.getId();
    }

    public CreateSnapshotRequestDto closeElection(UUID electionId) {
        Optional<Election> electionOpt = electionRepo.findById(electionId);
        if (electionOpt.isEmpty()) {
            throw new IllegalArgumentException("Election not found: " + electionId);
        }
        Election electionEntity = electionOpt.get();
        String electionContractAddress = electionEntity.getContractAddress();
        com.evote.backend.blockchain.contract.Election electionContract = contractLoader.loadElectionContract(electionContractAddress);

        txService.sendAndWait(
                "closeElection",
                () -> electionContract.closeElection()
        );

        electionEntity.setClosed(true);
        electionEntity.setStatus(ElectionStatus.COMPLETED);
        electionRepo.save(electionEntity);


        // provide snapshot dto object for the controller to Call merkle service for loading the merkle tree for this election

        return  new CreateSnapshotRequestDto(
                deploymentsConfig.deploymentsInfo().getSemaphoreAddress(),
                electionEntity.getSemaphoreGroupId().toString(),
                "10",
                electionEntity.getFirstRegistrationBlockNumber(),
                electionEntity.getLastRegistrationBlockNumber()
        );
    }

    public void closeRegistration(UUID electionId) {
        Optional<Election> electionOpt = electionRepo.findById(electionId);
        if (electionOpt.isEmpty()) {
            throw new IllegalArgumentException("Election not found: " + electionId);
        }
        Election electionEntity = electionOpt.get();

        //TODO add registration closing method in Election contract and call it here


        electionEntity.setStatus(ElectionStatus.REGISTRATION_COMPLETED);
        electionRepo.save(electionEntity);
    }

    public List<ElectionSummaryDto> getAllElections() {
        List<Election> elections = electionRepo.findAllByOrderByCreatedAtDesc();
        return elections.stream()
                .map(e -> toElectionSummaryDto(e))
                .toList();
    }
}