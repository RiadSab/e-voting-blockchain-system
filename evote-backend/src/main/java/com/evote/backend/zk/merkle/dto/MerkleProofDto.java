package com.evote.backend.zk.merkle.dto;

import lombok.Data;

// DTO for Merkle Proof, fetched from Merkle Service

@Data
public class MerkleProofDto {
    private String[] siblings;
    private Integer[] pathIndices;

    private Long leafIndex;
    private String rootHash;
    private Long depth;
}