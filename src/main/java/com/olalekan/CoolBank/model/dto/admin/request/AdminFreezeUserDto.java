package com.olalekan.CoolBank.model.dto.admin.request;

import jakarta.validation.constraints.NotBlank;

public record AdminFreezeUserDto(

        @NotBlank(message = "Reason cannot be blank")
        String reason
) {
}
