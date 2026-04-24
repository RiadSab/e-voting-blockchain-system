package com.evote.backend.election.controller;

import com.evote.backend.election.dto.ElectionDetailsDto;
import com.evote.backend.election.dto.ElectionSummaryDto;
import com.evote.backend.election.dto.RegisterCandidate;
import com.evote.backend.zk.merkle.dto.CreateSnapshotResponseDto;
import com.evote.backend.zk.merkle.dto.CreateSnapshotRequestDto;
import com.evote.backend.zk.merkle.dto.MerkleProofDto;
import com.evote.backend.zk.semaphore.dto.SemaphoreInputsDto;
import com.evote.backend.election.service.ElectionService;
import com.evote.backend.zk.merkle.service.MerkleClientService;
import com.evote.backend.vote.dto.SubmitVoteRequest;
import com.evote.backend.vote.dto.SubmitVoteResponse;
import com.evote.backend.vote.service.VoteService;
import com.evote.backend.voter.dto.VoterRegistrationRequest;
import com.evote.backend.voter.dto.VoterRegistrationResponse;
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
    private final MerkleClientService merkleClientService;

    // identityCommitment generated on client side using semaphore library from the seed provided by the user
    @GetMapping("/{electionId}/semaphore-inputs")
    public ResponseEntity<SemaphoreInputsDto> getSemaphoreInputs(@PathVariable UUID electionId, @RequestParam String identityCommitment) {

        SemaphoreInputsDto inputsDto = electionService.getSemaphoreInputs(electionId);
        MerkleProofDto merkleProofDto = merkleClientService.getMerkleProof(inputsDto.getSemaphoreAddress(), inputsDto.getGroupId(),identityCommitment);

        inputsDto.setSiblings(merkleProofDto.getSiblings());
        inputsDto.setPathIndices(merkleProofDto.getPathIndices());
        inputsDto.setLeafIndex(merkleProofDto.getLeafIndex());

        return ResponseEntity.ok(inputsDto);
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

    @PostMapping("/{electionId}/close-registration")
    public ResponseEntity<?> closeRegistration(@PathVariable UUID electionId) {
        electionService.closeRegistration(electionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ElectionSummaryDto>> getAllElections() {
        return ResponseEntity.ok(electionService.getAllElections());
    }
}