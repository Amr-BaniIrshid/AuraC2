package com.server.contestControl.contestServer.dto.problem;

import com.server.contestControl.contestServer.entity.Problem;
import com.server.contestControl.contestServer.enums.Difficulty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProblemResponse {
    private Long id;
    private String title;
    private String description;
    private Integer timeLimit;
    private Integer memoryLimit;
    private String difficulty;
    private Long contestId;
}