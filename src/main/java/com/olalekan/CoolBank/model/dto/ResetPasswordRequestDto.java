package com.olalekan.CoolBank.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordRequestDto(

        @NotBlank(message = "Token cannot be blank")
        @NotNull(message = "Token cannot be null")
        String token,
        @NotBlank(message = "New password cannot be blank")
        @NotNull(message = "New password cannot be null")
        String newPassword,
        @NotBlank(message = "Confirm password cannot be blank")
        @NotNull(message = "Confirm password cannot be null")
        String confirmPassword
) {
}
