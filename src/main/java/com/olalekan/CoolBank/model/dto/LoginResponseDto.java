package com.olalekan.CoolBank.model.dto;

import lombok.Builder;

@Builder
public record LoginResponseDto(

        String message,
        String accessToken,
        String refreshToken
) {
}
