package com.server.contestControl.contestServer.service;

import com.server.contestControl.contestServer.dto.contest.ContestRequest;
import com.server.contestControl.contestServer.dto.contest.ContestResponse;
import com.server.contestControl.contestServer.entity.Contest;
import com.server.contestControl.contestServer.enums.ContestStatus;
import com.server.contestControl.contestServer.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContestService {

    private final ContestRepository contestRepository;

    public ContestResponse createContest(ContestRequest request) {

        boolean existsActive = contestRepository
                .existsByStatusIn(List.of(ContestStatus.UPCOMING, ContestStatus.RUNNING));

        if (existsActive) {
            throw new RuntimeException("A contest is already scheduled or running. " +
                    "Please end it before creating a new one.");
        }



        Contest contest = Contest.builder()
                .title(request.title())
                .description(request.description())
                .startTime(request.startTime())
                .durationMinutes(request.durationMinutes())
                .status(ContestStatus.UPCOMING)
                .build();

        contestRepository.save(contest);
        return ContestResponse.fromEntity(contest);
    }

    public ContestResponse updateStatus(Long id, ContestStatus status) {
        Contest contest = contestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contest not found"));

        contest.setStatus(status);
        contestRepository.save(contest);

        return ContestResponse.fromEntity(contest);
    }

    public ContestResponse getActiveContest() {
        Contest contest = contestRepository.findByStatus(ContestStatus.RUNNING)
                .orElseThrow(() -> new RuntimeException("No active contest found"));

        return ContestResponse.fromEntity(contest);
    }


    public Contest getContestEntity() {
        Contest contest = contestRepository.findByStatus(ContestStatus.RUNNING)
                .orElseThrow(() -> new RuntimeException("No active contest found"));

        return contest;
    }
    public ContestResponse getUpcomingContest() {
        Contest contest = contestRepository.findByStatus(ContestStatus.UPCOMING)
                .orElseThrow(() -> new RuntimeException("No upcoming contest found"));

        return ContestResponse.fromEntity(contest);
    }
}
