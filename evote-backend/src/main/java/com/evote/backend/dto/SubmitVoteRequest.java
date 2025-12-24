package com.evote.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class SubmitVoteRequest {
    @NotBlank
    public String voterPublicKey;

    // Election id will be sent as a path variable, no need to include here

    @NotBlank private String c1x;
    @NotBlank private String c1y;
    @NotBlank private String c2x;
    @NotBlank private String c2y;

    @NotNull
    private SemaphoreProofDto semaphoreProof;
}
