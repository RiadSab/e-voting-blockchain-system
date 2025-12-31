package com.evote.backend.controller;

import com.evote.backend.dto.electionDto.ElectionCreateRequest;
import com.evote.backend.dto.electionDto.ElectionCreateResponse;
import com.evote.backend.service.ElectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/api/admin")
public class AdminController {
    private final ElectionService electionService;

    @PostMapping("/elections")
    public ResponseEntity<ElectionCreateResponse> createElection(@Valid @RequestBody ElectionCreateRequest request) {
        ElectionCreateResponse response = electionService.createElection(request);
        return ResponseEntity.status(201).body(response);
    }


}
