package com.olalekan.CoolBank.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDto(

        @Email(message = "Email must be a valid email")
        @NotBlank(message = "Email cannot be empty")
        String email
) {
}
