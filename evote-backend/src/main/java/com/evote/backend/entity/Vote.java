package com.evote.backend.entity;

import com.evote.backend.dto.VoteStatus;
import com.evote.backend.entity.shared.BaseEntitySeq;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "votes",
    indexes = {
            @Index(columnList = "election_id, submitted_at", name = "idx_votes_election"),
            @Index(columnList = "transaction_hash", name = "idx_votes_tx, unique", unique = true),
            @Index(columnList = "block_number", name = "idx_votes_block")
    },
    uniqueConstraints = {
        // can be specified via SQL DDL as well
            @UniqueConstraint(
                    name = "uk_votes_election_nullifier",
                    columnNames = {"election_id", "nullifier"}
            )
    })

public class Vote extends BaseEntitySeq {
    @Column(nullable = false)
    private UUID electionId;


    private String nullifier;

    private String c1x;
    private String c1y;
    private String c2x;
    private String c2y;

    private String transactionHash;

    private Long blockNumber;
    private Instant submittedAt;
    private VoteStatus status;
}
