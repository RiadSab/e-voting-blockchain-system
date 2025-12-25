package com.evote.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoterRegistrationResponse {
    private RegistrationStatus status;
    private String message;
    private String transactionHash;
}
