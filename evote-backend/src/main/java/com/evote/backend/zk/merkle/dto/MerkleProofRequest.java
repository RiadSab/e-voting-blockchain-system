package com.evote.backend.zk.merkle.dto;

// Request payload used by Merkle service proof endpoint.

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class MerkleProofRequest {
    private String semaphoreAddress;
    private String groupId;
    private String identityCommitment;
}

