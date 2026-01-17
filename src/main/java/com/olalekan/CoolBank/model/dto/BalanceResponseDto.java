package com.olalekan.CoolBank.model.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record BalanceResponseDto(
        UUID id,
        String fullName,
        BigDecimal balance
) {
}
