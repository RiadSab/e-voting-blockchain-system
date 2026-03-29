package com.evote.backend.zk.semaphore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// Client needs these inputs to generate a semaphore proof

@Data
@AllArgsConstructor @NoArgsConstructor
public class SemaphoreInputsDto {
    private String semaphoreAddress;
    private String groupId;
    private String depth;
    private String merkleRoot;
    private String[] siblings;
    private Integer[] pathIndices;
    private Long leafIndex;
}