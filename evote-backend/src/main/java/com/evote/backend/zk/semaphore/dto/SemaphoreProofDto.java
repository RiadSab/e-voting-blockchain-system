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
    private String message; // vote payload

    private String merkleTreeDepth;

    @NotBlank
    private String nullifier;

    @NotBlank
    private String scope; // eleeeection id

    @NotBlank
    private String merkleTreeRoot;

    @NotBlank
    private List<String> points; // 8 points
}
