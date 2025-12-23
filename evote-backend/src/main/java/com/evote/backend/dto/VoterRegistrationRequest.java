package com.evote.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VoterRegistrationRequest {
    @NotBlank
    private String electionId;

    @NotBlank
    private String identityCommitment;
}
