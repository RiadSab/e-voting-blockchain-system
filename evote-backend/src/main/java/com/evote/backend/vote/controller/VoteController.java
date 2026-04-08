package com.evote.backend.vote.controller;

import com.evote.backend.vote.dto.SubmitVoteRequest;
import com.evote.backend.vote.dto.SubmitVoteResponse;
import com.evote.backend.vote.service.VoteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/elections")
public class VoteController {
    private final VoteService voteService;
    @PostMapping("/{electionId}/votes")
    public ResponseEntity<SubmitVoteResponse> submitVote(
            @PathVariable UUID electionId,
            @Valid @RequestBody SubmitVoteRequest voteRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voteService.submitVote(voteRequest, electionId));
    }
}
