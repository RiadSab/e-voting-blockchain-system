package com.evote.backend.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
public class RegisterCandidate {
    private String firstName;
    private String lastName;
    private String partyName;
    private String partyId;
    private String manifestCid;
}
