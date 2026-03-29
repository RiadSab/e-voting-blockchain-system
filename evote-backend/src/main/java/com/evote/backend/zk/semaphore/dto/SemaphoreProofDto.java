package com.evote.backend.zk.semaphore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO representing a Semaphore proof, sent by the client when submitting a vote
// Used inside SubmitVoteRequest to split responsibilities


@Data
@AllArgsConstructor @NoArgsConstructor
public class SemaphoreProofDto {
    @NotBlank
    private String message;

    private String merkleTreeDepth;


    @NotBlank
    private String nullifier;


    @NotBlank
    private String scope;

    @NotBlank
    private String merkleRoot;

    @NotBlank
    private List<String> proof;
}
