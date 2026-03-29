package com.evote.backend.vote.dto;

import com.evote.backend.shared.enums.VoteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmitVoteResponse {
    private String transactionHash;
    private VoteStatus status;
    private Long timestamp;
    private String blockNumber;
    private String message;
}
