package com.evote.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmitVoteResponse {
    private String transactionHash;
    private VoteStatus status;
    private Long timestamp;
    private Long blockNumber;
    private String message;
}
