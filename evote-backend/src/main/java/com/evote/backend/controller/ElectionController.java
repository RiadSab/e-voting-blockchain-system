package com.evote.backend.controller;

import com.evote.backend.dto.SemaphoreInputsDto;
import com.evote.backend.dto.SubmitVoteRequest;
import com.evote.backend.dto.SubmitVoteResponse;
import com.evote.backend.service.ElectionService;
import com.evote.backend.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
