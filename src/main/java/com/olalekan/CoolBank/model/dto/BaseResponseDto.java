package com.olalekan.CoolBank.model.dto;

import lombok.Builder;

@Builder
public record BaseResponseDto(
        String message
) {
}
