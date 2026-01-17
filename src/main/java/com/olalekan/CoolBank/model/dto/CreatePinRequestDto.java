package com.olalekan.CoolBank.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePinRequestDto(
        @NotBlank(message = "Pin Cannot be empty")
        @Size(min = 4, max = 4, message = "Pin length cannot be less than or greater 4")
        String pin,

        @NotBlank(message = "Password cannot be empty")
        String password
) {
}
