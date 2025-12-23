package com.evote.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitVoteRequest {
    @NotBlank
    public String voterPublicKey;

    @NotBlank
    private String c1x;
    @NotBlank
    private String c1y;
    @NotBlank
    private String c2x;
    @NotBlank
    private String c2y;

    @NotNull
    private SemaphoreProofDto semaphoreProof;
}
