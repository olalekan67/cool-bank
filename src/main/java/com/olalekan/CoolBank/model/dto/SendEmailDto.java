package com.olalekan.CoolBank.model.dto;

import lombok.Builder;

@Builder
public record SendEmailDto(
        String toEmail,
        String subject,
        String body
) {
}
