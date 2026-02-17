package com.olalekan.CoolBank.model.dto.request;

import jakarta.validation.constraints.*;

public record RegisterRequestDto (
        @NotNull(message = "email cannot be null")
        @NotBlank(message = "email cannot be blank")
        @Email(message = "please enter a valid email")
        String email,
        @NotBlank(message = "full name cannot be blank")
        @NotNull(message = "full name cannot be null")
        String fullName,
        @NotBlank(message = "phone number cannot be blank")
        @NotNull(message = "phone number cannot be null")
        String phoneNumber,
        @NotBlank(message = "password cannot be blank")
        @NotNull(message = "password cannot be null")
        @Size(min = 8, max = 70, message = "Password length cannot be less than 8")
        String password
){
}
