package com.olalekan.CoolBank.model.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDto(

        @NotBlank(message = "Refresh token cannot be empty")
        String refreshToken
) {
}
