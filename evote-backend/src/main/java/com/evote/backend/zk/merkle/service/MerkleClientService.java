package com.evote.backend.zk.merkle.service;

import com.evote.backend.zk.merkle.dto.CreateSnapshotResponseDto;
import com.evote.backend.zk.merkle.dto.CreateSnapshotRequestDto;
import com.evote.backend.zk.merkle.dto.MerkleProofDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MerkleClientService {
    private final RestClient restClient;

    public MerkleClientService(@Value(("${merkle-service.url}")) String merkleServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(merkleServiceUrl)
                .build();
    }

    public CreateSnapshotResponseDto createSnapshot(CreateSnapshotRequestDto requestDto) {
        return restClient.post()
                .uri("/internal/groups/snapshot")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .retrieve()
                .body(CreateSnapshotResponseDto.class);
    }


    public MerkleProofDto getMerkleProof(String semaphoreAddress, String groupId, String identityCommitment) {
        var requestBody = new MerkleProofRequest(semaphoreAddress, groupId, identityCommitment);

        // using post request to avoid url length limitations and firewall issues

        return restClient.post()
                .uri("/internal/groups/merkle-proof")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(MerkleProofDto.class);
    }
    private record MerkleProofRequest(String semaphoreAddress, String groupId, String identityCommitment) {}
}