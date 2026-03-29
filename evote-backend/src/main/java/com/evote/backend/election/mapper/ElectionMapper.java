package com.evote.backend.election.mapper;

import com.evote.backend.election.dto.ElectionCreateRequest;
import com.evote.backend.shared.enums.ElectionStatus;
import com.evote.backend.election.entity.Election;

import java.math.BigInteger;

public class ElectionMapper {
    public static Election toElectionEntity(ElectionCreateRequest request, Long semaphoreGroupId, String contractAddress, BigInteger electionIdOnChain) {
        Election entity = new Election();
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