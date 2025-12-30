package com.evote.backend.entity.shared;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@MappedSuperclass
@Data
public abstract class Auditable {

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt = Instant.now();

    @UpdateTimestamp
    private Instant updatedAt;

    @Version
    private Long version;
}
