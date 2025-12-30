package com.evote.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ElectionCreateResponse {
    private String electionId;
    private String message;
    private ElectionStatus status;
    private String transactionHash;
    private String electionAddress;
    private Long createdAt;
    private Long blockNumber;
}
