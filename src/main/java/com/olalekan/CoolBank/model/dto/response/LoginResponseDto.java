package com.olalekan.CoolBank.model.dto.response;

import lombok.Builder;

@Builder
public record LoginResponseDto(

        String message,
        String accessToken,
        String refreshToken
) {
}
