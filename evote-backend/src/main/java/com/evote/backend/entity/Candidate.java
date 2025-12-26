package com.evote.backend.entity;

import com.evote.backend.entity.shared.BaseEntityUuid;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Candidate extends BaseEntityUuid {
    private UUID electionId;
    private String firstName;
    private String lastName;
    private String partyName;
    private String partyId;
    private String manifestCid;
}
