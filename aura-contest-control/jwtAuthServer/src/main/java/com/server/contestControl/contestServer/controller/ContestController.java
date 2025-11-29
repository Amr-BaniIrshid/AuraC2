package com.server.contestControl.contestServer.controller;

import com.server.contestControl.contestServer.dto.contest.ContestRequest;
import com.server.contestControl.contestServer.dto.contest.ContestResponse;
import com.server.contestControl.contestServer.enums.ContestStatus;
import com.server.contestControl.contestServer.service.ContestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contest")
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContestResponse> createContest(
            @RequestBody ContestRequest request) {

        return ResponseEntity.ok(contestService.createContest(request));
    }

    @PutMapping("/{id}/start")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContestResponse> startContest(@PathVariable Long id) {
        return ResponseEntity.ok(contestService.updateStatus(id, ContestStatus.RUNNING));
    }

    @PutMapping("/{id}/pause")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContestResponse> pauseContest(@PathVariable Long id) {
        return ResponseEntity.ok(contestService.updateStatus(id, ContestStatus.PAUSED));
    }

    @PutMapping("/{id}/end")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContestResponse> endContest(@PathVariable Long id) {
        return ResponseEntity.ok(contestService.updateStatus(id, ContestStatus.ENDED));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('TEAM', 'ADMIN')")
    public ResponseEntity<ContestResponse> getActiveContest() {
        return ResponseEntity.ok(contestService.getActiveContest());
    }

    @GetMapping("/upcoming")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ContestResponse> getUpcomingContest() {
        return ResponseEntity.ok(contestService.getUpcomingContest());
    }


}

