package com.olalekan.CoolBank.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDto(

        @NotBlank(message = "Refresh token cannot be empty")
        String refreshToken
) {
}
