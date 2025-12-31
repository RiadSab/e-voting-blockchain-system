package com.evote.backend.dto.votingDto;

import com.evote.backend.dto.enums.VoteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class SubmitVoteResponse {
    private String transactionHash;
    private VoteStatus status;
    private Long timestamp;
    private BigInteger blockNumber;
    private String message;
}
