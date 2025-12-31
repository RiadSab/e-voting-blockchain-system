package com.evote.backend.dto.votingDto;

import com.evote.backend.dto.enums.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoterRegistrationResponse {
    private RegistrationStatus status;
    private String message;
    private String transactionHash;
}
