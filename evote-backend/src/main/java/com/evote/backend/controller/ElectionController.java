package com.evote.backend.controller;

import com.evote.backend.dto.electionDto.ElectionDetailsDto;
import com.evote.backend.dto.electionDto.ElectionSummaryDto;
import com.evote.backend.dto.electionDto.RegisterCandidate;
import com.evote.backend.dto.merkleServiceDto.CreateSnapshotResponseDto;
import com.evote.backend.dto.merkleServiceDto.CreateSnapshotRequestDto;
import com.evote.backend.dto.merkleServiceDto.MerkleProofDto;
import com.evote.backend.dto.clientSemaphoreDto.SemaphoreInputsDto;
import com.evote.backend.dto.votingDto.*;
import com.evote.backend.service.ElectionService;
import com.evote.backend.service.MerkleClientService;
import com.evote.backend.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/elections")
public class ElectionController {
    private final ElectionService electionService;
    private final VoteService voteService;
    private final MerkleClientService merkleClientService;

    // identityCommitment generated on client side using semaphore library from the seed provided by the user
    @PostMapping("/{electionId}/semaphore-inputs")
    public ResponseEntity<SemaphoreInputsDto> getSemaphoreInputs(@PathVariable UUID electionId, @RequestParam String identityCommitment) {

        SemaphoreInputsDto inputsDto = electionService.getSemaphoreInputs(electionId);
        MerkleProofDto merkleProofDto = merkleClientService.getMerkleProof(inputsDto.getSemaphoreAddress(), inputsDto.getGroupId(),identityCommitment);

        inputsDto.setSiblings(merkleProofDto.getSiblings());
        inputsDto.setPathIndices(merkleProofDto.getPathIndices());
        inputsDto.setLeafIndex(merkleProofDto.getLeafIndex());

        return ResponseEntity.ok(inputsDto);
    }


    @PostMapping("/{electionId}/votes")
    public ResponseEntity<SubmitVoteResponse> submitVote(
            @PathVariable UUID electionId,
            @Valid @RequestBody SubmitVoteRequest voteRequest
    ) {
        return ResponseEntity.ok(voteService.submitVote(voteRequest, electionId));
    }


    //TODO : Store commitments in DB to prevent duplicates and gas fees
    @PostMapping("/{electionId}/registration")
    public ResponseEntity<VoterRegistrationResponse> registerVoter(
            @PathVariable UUID electionId,
            @Valid @RequestBody VoterRegistrationRequest registrationRequest
            ) {
        return ResponseEntity.status(201).body(electionService.registerVoter(electionId, registrationRequest));
    }

    @GetMapping("/open")
    public ResponseEntity<List<ElectionSummaryDto>> getOpenElections() {
        return ResponseEntity.ok(electionService.getOpenElections());
    }

    @GetMapping("/{electionId}")
    public ResponseEntity<ElectionDetailsDto> getElectionDetails(@PathVariable UUID electionId) {
        return ResponseEntity.ok(electionService.getElectionDetails(electionId));
    }


    @PostMapping("/{electionId}/candidates")
    public ResponseEntity<UUID> registerCandidate(
            @PathVariable UUID electionId,
            @Valid @RequestBody RegisterCandidate request
    )
    {
        return ResponseEntity.status(201).body(electionService.registerCandidate(electionId, request));
    }

    // close election, trigger snapshot creation on merkle service and return snapshot details
    @PostMapping("/{electionId}/close")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateSnapshotResponseDto> closeElection(@PathVariable UUID electionId) {
        CreateSnapshotRequestDto req = electionService.closeElection(electionId);
        CreateSnapshotResponseDto res = merkleClientService.createSnapshot(req);

        return ResponseEntity.ok(res);
    }


}