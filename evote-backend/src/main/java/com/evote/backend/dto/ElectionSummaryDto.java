package com.evote.backend.dto;

import lombok.Data;

@Data
public class ElectionSummaryDto {
    private String electionId;
    private String contractAddress;
    private String metadataHash;
    private Long startTime;
    private Long endTime;
    private ElectionStatus status;
    private boolean closed;
    private boolean tallyPublished;
}