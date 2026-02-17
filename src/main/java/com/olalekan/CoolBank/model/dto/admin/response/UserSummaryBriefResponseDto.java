package com.olalekan.CoolBank.model.dto.admin.response;

import lombok.Builder;

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
