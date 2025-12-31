package com.evote.backend.dto.electionDto;

import com.evote.backend.dto.enums.ElectionStatus;
import com.evote.backend.entity.Candidate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data @AllArgsConstructor
public class ElectionDetailsDto {

    // We can fetch the metadata from database and sent it along with CID for reliability

    private String electionId;
    private String electionName;
    private String contractAddress;
    private String metadataCid;
    private String metadataHash;
    private Long startTime;
    private Long endTime;
    private ElectionStatus status;
    private String semaphoreGroupId;
    private boolean tallyPublished;
    private List<Candidate> candidates;
}
