package com.evote.backend.election.entity;

import com.evote.backend.shared.enums.ElectionStatus;
import com.evote.backend.candidate.entity.Candidate;
import com.evote.backend.shared.base.BaseEntityUuid;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "elections")

public class Election extends BaseEntityUuid {

    private String electionName;
    private String electionIdOnChain; //todo: find out if we need this field
    private String contractAddress;
    private String metadataCid;
    private String metadataHash;
    private Long semaphoreGroupId;
    private Long startTime;
    private Long endTime;
    private ElectionStatus status;
    private boolean closed;
    private boolean tallyPublished;
    List<Candidate> candidates;

    private boolean firstBlockFetched;
    private String firstRegistrationBlockNumber;
    private String lastRegistrationBlockNumber;
    private String rootHash;
    private Long totalVoterCount;
}
