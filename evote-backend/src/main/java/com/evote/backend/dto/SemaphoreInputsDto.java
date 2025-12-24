package com.evote.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class SemaphoreInputsDto {
    private String semaphoreAddress;
    private String groupId;
    private String depth;
    private String merkleRoot;
}