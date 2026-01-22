package com.olalekan.CoolBank.model.dto.admin;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AdminActionLogResponseDto(
        UUID id,
        String adminName,
        String action,
        String targetId,
        String reason,
        LocalDateTime createdAt
) {
}
