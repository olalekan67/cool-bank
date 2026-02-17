package com.olalekan.CoolBank.model.dto.admin.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserSummaryResponseDto(
        UUID userId,
        String fullName,
        String email,
        String phoneNumber,
        String kycTier,
        String activeStatus,
        BigDecimal walletBalance,
        LocalDateTime createdAt
) {
}
