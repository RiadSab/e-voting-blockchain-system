package com.evote.backend.dto;

import lombok.Data;

@Data
public class ElectionDetailsDto {

    // We can fetch the metadata from database and sent it along with CID for reliability

    private String electionId;
    private String contractAddress;
    private String metadataCid;
    private String metadataHash;
    private Long startTime;
    private Long endTime;
    private ElectionStatus status;
    private String semaphoreGroupId;
    private boolean tallyPublished;
}
