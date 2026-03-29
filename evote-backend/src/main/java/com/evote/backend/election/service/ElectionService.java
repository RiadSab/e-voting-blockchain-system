package com.evote.backend.election.service;

import com.evote.backend.blockchain.config.DeploymentsConfig;
import com.evote.backend.blockchain.contract.ElectionFactory;
import com.evote.backend.blockchain.contract.Semaphore;
import com.evote.backend.shared.enums.ElectionStatus;
import com.evote.backend.shared.enums.RegistrationStatus;
import com.evote.backend.zk.merkle.dto.CreateSnapshotRequestDto;
import com.evote.backend.zk.semaphore.dto.SemaphoreInputsDto;
import com.evote.backend.voter.dto.VoterRegistrationRequest;
import com.evote.backend.voter.dto.VoterRegistrationResponse;
import com.evote.backend.election.dto.*;
import com.evote.backend.candidate.entity.Candidate;
import com.evote.backend.election.entity.Election;
import com.evote.backend.blockchain.tx.TxResult;
import com.evote.backend.blockchain.exception.BlockchainTxException;
import com.evote.backend.voter.exception.VoterAlreadyRegisteredException;
import com.evote.backend.blockchain.factory.ContractLoader;
import static  com.evote.backend.shared.mapper.SharedMapper.toBigInt;
import static com.evote.backend.election.mapper.ElectionMapper.toElectionEntity;

import com.evote.backend.candidate.repository.CandidateRepository;
import com.evote.backend.election.repository.ElectionRepository;
import com.evote.backend.blockchain.service.BlockchainTransactionService;
import com.evote.backend.blockchain.service.BlockchainViewService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
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
                .map(e -> new ElectionSummaryDto(
                        e.getId().toString(),
                        e.getElectionName(),
                        e.getContractAddress(),
                        e.getMetadataCid(),
                        e.getStartTime(),
                        e.getEndTime(),
                        e.getStatus(),
                        e.isClosed(),
                        e.isTallyPublished()
                ))
                .toList();
    }

    public ElectionDetailsDto getElectionDetails(UUID electionId) {
        Optional<Election> electionOpt = electionRepo.findById(electionId);
        if (electionOpt.isEmpty()) {
            throw new IllegalArgumentException("Election not found: " + electionId);
        }
        Election election = electionOpt.get();
        return new ElectionDetailsDto(
                election.getId().toString(),
                election.getElectionName(),
                election.getContractAddress(),
                election.getMetadataCid(),
                election.getMetadataHash(),
                election.getStartTime(),
                election.getEndTime(),
                election.getStatus(),
                election.getSemaphoreGroupId().toString(),
                election.isTallyPublished(),
                election.getCandidates()
        );
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
                .map(e -> new ElectionSummaryDto(
                        e.getId().toString(),
                        e.getElectionName(),
                        e.getContractAddress(),
                        e.getMetadataCid(),
                        e.getStartTime(),
                        e.getEndTime(),
                        e.getStatus(),
                        e.isClosed(),
                        e.isTallyPublished()
                ))
                .toList();
    }
}