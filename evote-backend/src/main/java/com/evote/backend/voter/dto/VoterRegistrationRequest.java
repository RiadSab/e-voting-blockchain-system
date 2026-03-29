package com.evote.backend.voter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VoterRegistrationRequest {

    @NotBlank
    private String identityCommitment;
}
