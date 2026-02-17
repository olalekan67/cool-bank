package com.olalekan.CoolBank.model.dto.response;

import lombok.Builder;

@Builder
public record IntialisePaymentResponse(
        String authorizationUrl
){
}
