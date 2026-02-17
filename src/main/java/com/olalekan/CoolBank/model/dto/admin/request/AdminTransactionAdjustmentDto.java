package com.olalekan.CoolBank.model.dto.admin.request;


import com.olalekan.CoolBank.Utils.TransactionType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AdminTransactionAdjustmentDto(

        @Positive(message = "Amount cannot be a negative number")
        BigDecimal amount,

        @Email(message = "This is an invalid email")
        @NotBlank(message = "Email cannot be blank")
        String email,
        @NotBlank(message = "Transaction type cannot be blank")
        @NotNull(message = "Transaction type cannot be Null")
        TransactionType type,
        @NotBlank(message = "Description cannot be blank")
        @NotNull(message = "Description cannot be Null")
        String description,
        @NotBlank(message = "Reason cannot be blank")
        @NotNull(message = "Reason cannot be Null")
        String reason
) {
}
