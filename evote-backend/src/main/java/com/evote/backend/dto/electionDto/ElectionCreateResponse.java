package com.evote.backend.dto.electionDto;

import com.evote.backend.dto.enums.ElectionStatus;
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
