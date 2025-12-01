package com.evote.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class VoteRequest {
    @NotNull(message = "Commitment cannot be null")
    @Pattern(regexp = "^0x[a-fA-F0-9]{64}$", message = "Commitment must be a valid 32-byte hexadecimal string prefixed with 0x")
    private String commitment;
}
