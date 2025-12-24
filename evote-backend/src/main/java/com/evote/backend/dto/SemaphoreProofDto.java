package com.evote.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class SemaphoreProofDto {
    @NotBlank
    private String message;

    private String merkleTreeDepth;


    @NotBlank
    private String nullifier;


    @NotBlank
    private String scope;

    @NotBlank
    private String merkleRoot;

    @NotBlank
    private List<String> proof;
}
