package com.evote.backend.entity.shared;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;

import java.time.Instant;

@MappedSuperclass
@Data
public abstract class Auditable {

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
    private Instant updatedAt;

    @Version
    private Long version;
}
