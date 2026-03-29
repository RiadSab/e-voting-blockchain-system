package com.evote.backend.election.dto;

import com.evote.backend.shared.enums.ElectionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ElectionCreateResponse {
    private String electionId;
    private String message;
    private ElectionStatus status;
    private String transactionHash;
    private String electionAddress;
    private String createdAt;
    private String blockNumber;
}
