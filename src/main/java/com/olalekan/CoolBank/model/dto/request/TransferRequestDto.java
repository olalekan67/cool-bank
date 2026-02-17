package com.olalekan.CoolBank.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequestDto(
        @NotBlank(message = "Recipient email cannot be blank")
        @NotNull(message = "Recipient email cannot be null")
        @Email(message = "Recipient email must be a valid email")
        String recipientEmail,
        @Positive(message = "Amount cannot be a negative number")
        BigDecimal amount,
        String description,
        @NotBlank(message = "Pin cannot be blank")
        String pin,

        @NotBlank(message = "reference cannot be empty")
        String reference
) {
}
