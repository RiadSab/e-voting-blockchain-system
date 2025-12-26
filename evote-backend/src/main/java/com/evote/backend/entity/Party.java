package com.evote.backend.entity;

import com.evote.backend.entity.shared.BaseEntityUuid;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Party extends BaseEntityUuid {
    private String name;
}
