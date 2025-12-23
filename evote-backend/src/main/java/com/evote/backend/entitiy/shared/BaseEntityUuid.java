package com.evote.backend.entitiy.shared;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class BaseEntityUuid extends Auditable {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();
}
