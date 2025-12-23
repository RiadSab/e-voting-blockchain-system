package com.evote.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ElectionCreateRequest {
    @NotBlank
    private String ipfsCid;

    @NotBlank
    private String metadataHash;

    @NotBlank
    private String pkx;

    @NotBlank
    private String pky;

    @NotBlank
    private Long startTime;

    @NotBlank
    private Long endTime;

    @NotBlank
    private String electoralAuthority;

    private String tallyVerifier; // we can use the authority as default tally verifier

    // Semaphore address: passed by the Factory
    // Semaphore groupId: created internally by Factory using Semaphore interface
}
