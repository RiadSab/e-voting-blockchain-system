package com.evote.backend.controller;

import com.evote.backend.dto.*;
import com.evote.backend.service.ElectionService;
import com.evote.backend.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/elections")
public class ElectionController {
    private final ElectionService electionService;
    private final VoteService voteService;

    @GetMapping("/{electionId}/semaphore-inputs")
    public ResponseEntity<SemaphoreInputsDto> getSemaphoreInputs(@PathVariable UUID electionId) {
        return ResponseEntity.ok(electionService.getSemaphoreInputs(electionId));
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
    ) {
        return ResponseEntity.status(201).body(electionService.registerCandidate(electionId, request));
    }
}