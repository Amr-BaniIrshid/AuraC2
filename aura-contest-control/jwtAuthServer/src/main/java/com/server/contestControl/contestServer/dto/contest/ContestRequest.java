package com.server.contestControl.contestServer.dto.contest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

public record ContestRequest(
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("startTime") LocalDateTime startTime,
        @JsonProperty("durationMinutes") Integer durationMinutes
) {}