package com.olalekan.CoolBank.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequestDto(
        @NotBlank(message = "Recipient email cannot be blank")
        @NotNull(message = "Recipient email cannot be null")
        @Email(message = "Recipient email must be a valid email")
        String recipientEmail,
        @NotBlank(message = "Amount cannot be blank")
        BigDecimal amount,
        String description,
        @NotBlank(message = "Pin cannot be blank")
        String pin,

        @NotBlank(message = "reference cannot be empty")
        String reference
) {
}
