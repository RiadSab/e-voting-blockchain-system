package com.evote.backend.repository;

import com.evote.backend.entity.ElectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ElectionRepository extends JpaRepository<ElectionEntity, UUID> {
    Optional<String> findContractAddressById(UUID id);

    List<ElectionEntity> findByClosedFalseOrderByCreatedAtDesc();

    List<ElectionEntity> findAllByOrderByCreatedAtDesc();
}