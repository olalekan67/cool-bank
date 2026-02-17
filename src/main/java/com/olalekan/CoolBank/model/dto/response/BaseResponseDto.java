package com.olalekan.CoolBank.model.dto.response;

import lombok.Builder;

@Builder
public record BaseResponseDto(
        String message
) {
}
