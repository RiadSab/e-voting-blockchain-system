package com.evote.backend.entitiy;

import com.evote.backend.entitiy.shared.BaseEntityUuid;
import jakarta.persistence.Entity;

import java.util.UUID;

@Entity
public class Candidate extends BaseEntityUuid {
    private UUID electionId;
    private String firstName;
    private String lastName;
    private String party;
    private String manifestCid;
}
