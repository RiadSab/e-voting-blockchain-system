package com.evote.backend.election.dto;

import lombok.Getter;


@Getter
public class RegisterCandidate {
    private String firstName;
    private String lastName;
    private String partyName;
    private String partyId;
    private String manifestCid;
}