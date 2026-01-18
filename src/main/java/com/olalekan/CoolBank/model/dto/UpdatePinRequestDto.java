package com.olalekan.CoolBank.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePinRequestDto(
        @NotBlank(message = "Old pin Cannot be empty")
        @Size(min = 4, max = 4, message = "Old pin length cannot be less than or greater 4")
        String oldPin,

        @NotBlank(message = "New pin Cannot be empty")
        @Size(min = 4, max = 4, message = "New pin length cannot be less than or greater 4")
        String newPin,

        @NotBlank(message = "Password cannot be empty")
        String password
) {
}
