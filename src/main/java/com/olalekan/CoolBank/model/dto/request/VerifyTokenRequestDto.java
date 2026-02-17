package com.olalekan.CoolBank.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VerifyTokenRequestDto(

        @NotNull(message = "Token cannot be null")
        @NotBlank(message = "Token cannot be blank")
        String token
) {
}
