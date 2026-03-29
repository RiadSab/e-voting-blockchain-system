package com.evote.backend.election.dto;

import com.evote.backend.shared.enums.ElectionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ElectionSummaryDto {
    private String electionId;
    private String electionName;
    private String contractAddress;
    private String metadataCid;
    private Long startTime;
    private Long endTime;
    private ElectionStatus status;
    private boolean closed;
    private boolean tallyPublished;
}