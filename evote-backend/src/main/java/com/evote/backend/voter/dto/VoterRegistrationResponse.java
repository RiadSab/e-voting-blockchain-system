package com.evote.backend.voter.dto;

import com.evote.backend.shared.enums.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoterRegistrationResponse {
    private RegistrationStatus status;
    private String message;
    private String transactionHash;
}
