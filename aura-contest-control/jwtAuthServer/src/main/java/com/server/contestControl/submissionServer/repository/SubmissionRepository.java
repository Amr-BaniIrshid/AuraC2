package com.server.contestControl.submissionServer.repository;

import com.server.contestControl.submissionServer.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}
