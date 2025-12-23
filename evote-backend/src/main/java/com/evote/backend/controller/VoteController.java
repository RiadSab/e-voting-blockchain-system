package com.evote.backend.controller;

import com.evote.backend.dto.SubmitVoteRequest;
import com.evote.backend.dto.SubmitVoteResponse;
import com.evote.backend.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:5173")
public class VoteController {
//    private final VoteService voteService;
//
//    public VoteController(VoteService voteService) {
//        this.voteService = voteService;
//    }

//    @PostMapping("/vote")
//    public ResponseEntity<SubmitVoteResponse> submitVote(@RequestBody  @Valid SubmitVoteRequest voteRequest) {
//
//        String txHash = voteService.castVote(voteRequest);
//
//        return ResponseEntity.ok(new SubmitVoteResponse("Vote submitted successfully", txHash));
//    }
}