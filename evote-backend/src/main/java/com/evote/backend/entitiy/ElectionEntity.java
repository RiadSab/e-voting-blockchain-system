package com.evote.backend.entitiy;

import com.evote.backend.dto.ElectionStatus;
import com.evote.backend.entitiy.shared.BaseEntityUuid;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "elections")

public class ElectionEntity extends BaseEntityUuid {

    private String electionName;
    private String electionIdOnChain; //todo: found out if we need this field
    private String contractAddress;
    private String metadataCid;
    private String metadataHash;
    private Long semaphoreGroupId;
    private Long startTime;
    private Long endTime;
    private ElectionStatus status;
    private boolean closed;
    private boolean tallyPublished;

}
