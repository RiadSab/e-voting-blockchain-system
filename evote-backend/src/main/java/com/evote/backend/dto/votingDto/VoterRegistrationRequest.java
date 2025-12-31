package com.evote.backend.dto.votingDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VoterRegistrationRequest {

    @NotBlank
    private String identityCommitment;
}
