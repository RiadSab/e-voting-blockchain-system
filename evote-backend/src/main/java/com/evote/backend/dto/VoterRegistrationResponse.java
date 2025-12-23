package com.evote.backend.dto;

import lombok.Data;

@Data
public class VoterRegistrationResponse {
    private RegistrationStatus status;
    private String message;
    private String transactionHash;
}
