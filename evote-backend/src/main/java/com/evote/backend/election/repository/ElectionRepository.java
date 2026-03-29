package com.evote.backend.election.repository;

import com.evote.backend.election.entity.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ElectionRepository extends JpaRepository<Election, UUID> {
    Optional<String> findContractAddressById(UUID id);

    List<Election> findByClosedFalseOrderByCreatedAtDesc();

    List<Election> findAllByOrderByCreatedAtDesc();
}