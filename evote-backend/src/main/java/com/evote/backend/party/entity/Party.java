package com.evote.backend.party.entity;

import com.evote.backend.shared.base.BaseEntityUuid;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Party extends BaseEntityUuid {
    private String name;
}
