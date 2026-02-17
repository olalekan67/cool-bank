package com.olalekan.CoolBank.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WithdrawalRequestDto(
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "1000", message = "The minimum withdrawal amount is NGN 1000")
        BigDecimal amount,
        @NotBlank(message = "Bank code is required")
        String bankCode,
        @NotBlank(message = "Account number is required")
        String accountNumber,
        @NotBlank(message = "Account name is required")
        String accountName,
        @NotBlank(message = "Pin is required")
        String pin
) {
}
