package com.olalekan.CoolBank.model.dto.admin;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Builder

public record UserSummaryBriefResponseDto(
        UUID userId,
        String fullName,
        String email,
        String phoneNumber,
        String kycTier
) {
}
