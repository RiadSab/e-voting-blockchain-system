package com.evote.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteResponse {
    private String message;
    private String transactionHash;
}
