package com.evote.backend.zk.merkle.dto;

import lombok.AllArgsConstructor;

// DTO for creating a snapshot request to Merkle Service

@AllArgsConstructor
public class CreateSnapshotRequestDto {
    private String semaphoreAddress;
    private String groupId;
    private String depth;
    private String fromBlock;
    private String toBlock;
}
