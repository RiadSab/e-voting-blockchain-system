package com.evote.backend.repository;

import com.evote.backend.entitiy.ElectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ElectionRepository extends JpaRepository<ElectionEntity, UUID> {
    Optional<String> findAddressById(UUID id);
}