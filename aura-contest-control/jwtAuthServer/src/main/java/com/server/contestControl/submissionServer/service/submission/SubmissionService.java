package com.server.contestControl.submissionServer.service.submission;

import com.server.contestControl.authServer.entity.User;
import com.server.contestControl.authServer.exception.api.UserNotFoundException;
import com.server.contestControl.authServer.repository.UserRepository;
import com.server.contestControl.authServer.service.user.UserService;
import com.server.contestControl.contestServer.dto.contest.ContestResponse;
import com.server.contestControl.contestServer.dto.problem.ProblemResponse;
import com.server.contestControl.contestServer.entity.Contest;
import com.server.contestControl.contestServer.entity.Problem;
import com.server.contestControl.contestServer.service.ContestService;
import com.server.contestControl.contestServer.service.ProblemService;
import com.server.contestControl.submissionServer.dto.SubmissionRequest;
import com.server.contestControl.submissionServer.dto.SubmissionResponse;
import com.server.contestControl.submissionServer.entity.Submission;
import com.server.contestControl.submissionServer.enums.Verdict;
import com.server.contestControl.submissionServer.queue.submission.SubmissionProducer;
import com.server.contestControl.submissionServer.repository.SubmissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final SubmissionProducer submissionProducer;

    private final ContestService contestService;
    private final ProblemService problemService;
    private final UserRepository userRepository;

    @Transactional
    public SubmissionResponse submitCode(SubmissionRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UserNotFoundException(auth.getName()));

        Contest contest = contestService.getContestEntity();
        Problem problem = problemService.getProblemEntity(request.problemId());


        Submission submission = Submission.builder()
                .contest(contest)
                .problem(problem)
                .user(user)
                .code(request.code())
                .language(request.language())
                .verdict(Verdict.PENDING)
                .build();

        submissionRepository.save(submission);

        submissionProducer.sendSubmission(submission.getId());

        return SubmissionResponse.fromEntity(submission);
    }

    public SubmissionResponse getSubmissionById(Long id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        return SubmissionResponse.fromEntity(submission);
    }
}
