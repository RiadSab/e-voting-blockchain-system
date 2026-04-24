package com.evote.backend.election.mapper;

import com.evote.backend.election.dto.ElectionCreateRequest;
import com.evote.backend.election.dto.ElectionDetailsDto;
import com.evote.backend.election.dto.ElectionSummaryDto;
import com.evote.backend.shared.enums.ElectionStatus;
import com.evote.backend.election.entity.Election;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
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

    public static ElectionSummaryDto toElectionSummaryDto(Election election) {
        return new ElectionSummaryDto(
                election.getId().toString(),
                election.getElectionName(),
                election.getContractAddress(),
                election.getMetadataCid(),
                election.getStartTime(),
                election.getEndTime(),
                election.getStatus(),
                election.isClosed(),
                election.isTallyPublished()
        );
    }

    public static ElectionDetailsDto toElectionDetailsDto(Election election) {
        return new ElectionDetailsDto(
                election.getId().toString(),
                election.getElectionName(),
                election.getContractAddress(),
                election.getMetadataCid(),
                election.getMetadataHash(),
                election.getStartTime(),
                election.getEndTime(),
                election.getStatus(),
                election.getSemaphoreGroupId() != null ? election.getSemaphoreGroupId().toString() : null,
                election.isTallyPublished(),
                election.getCandidates()
        );
    }
}