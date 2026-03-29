package com.evote.backend.zk.merkle.dto;

import lombok.Data;

// Response DTO for creating a snapshot in the Merkle tree service

@Data
public class CreateSnapshotResponseDto {
    private String snapshotId;
    private String rootHash;
    private Long membersCount;

}