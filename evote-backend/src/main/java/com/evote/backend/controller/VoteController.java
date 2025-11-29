package com.evote.backend.controller;

import com.evote.backend.dto.VoteRequest;
import com.evote.backend.dto.VoteResponse;
import com.evote.backend.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/vote")
    public ResponseEntity<VoteResponse> submitVote(@RequestBody  @Valid  VoteRequest voteRequest) {

        String txHash = voteService.castVote(voteRequest);

        return ResponseEntity.ok(new VoteResponse("Vote submitted successfully", txHash));
    }
}
