package com.olalekan.CoolBank.model.dto.admin.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AdminDashboardStatDto(
        Long totalUsers,
        Long activeUser,
        BigDecimal totalTransactionVolume,
        Long totalTransactionCount,
        Long pendingKycCount
) {
}
