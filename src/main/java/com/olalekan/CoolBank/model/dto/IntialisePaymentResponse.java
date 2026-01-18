package com.olalekan.CoolBank.model.dto;

import lombok.Builder;

@Builder
public record IntialisePaymentResponse(
        String authorizationUrl
){
}
