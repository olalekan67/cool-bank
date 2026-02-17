package com.olalekan.CoolBank.model.dto.request;


import jakarta.validation.constraints.Positive;

public record FundRequestDto(
        @Positive(message = "amount cannot be a negative number")
        int amount
) {
}
