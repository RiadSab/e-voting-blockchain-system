package com.evote.backend.mapper;

import com.evote.backend.dto.ElectionCreateRequest;
import com.evote.backend.dto.ElectionStatus;
import com.evote.backend.entity.ElectionEntity;

import java.math.BigInteger;

public class ElectionMapper {
    public static ElectionEntity toElectionEntity(ElectionCreateRequest request, Long semaphoreGroupId, String contractAddress, BigInteger electionIdOnChain) {
        ElectionEntity entity = new ElectionEntity();
        entity.setMetadataCid(request.getIpfsCid());
        entity.setMetadataHash(request.getMetadataHash());
        entity.setStartTime(request.getStartTime());
        entity.setEndTime(request.getEndTime());
        entity.setClosed(true);
        entity.setElectionName(request.getElectionName());
        entity.setStatus(ElectionStatus.UPCOMING);
        entity.setTallyPublished(false);
        entity.setContractAddress(contractAddress);
        entity.setSemaphoreGroupId(semaphoreGroupId);
        entity.setElectionIdOnChain(electionIdOnChain.toString());
        return entity;
    }
}