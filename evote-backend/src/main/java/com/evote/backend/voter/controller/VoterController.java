package com.evote.backend.voter.controller;

import com.evote.backend.voter.dto.VoterRegistrationRequest;
import com.evote.backend.voter.dto.VoterRegistrationResponse;
import com.evote.backend.voter.service.VoterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/elections/{electionId}/voters")
@RequiredArgsConstructor
public class VoterController {

    private final VoterService voterService;

    @PostMapping
    public ResponseEntity<VoterRegistrationResponse> registerVoter(
            @PathVariable UUID electionId,
            @Valid @RequestBody VoterRegistrationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(voterService.registerVoter(electionId, request));
    }
}
