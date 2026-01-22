package com.olalekan.CoolBank.model.dto.admin;

import com.olalekan.CoolBank.Utils.KycTier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountKycUpgradeDto(

        @NotBlank(message = "KYC tier cannot be blank")
        KycTier tier,
        @NotBlank(message = "Reason cannot be blank")
        @NotNull(message = "Reason cannot be null")
        String reason
) {
}
