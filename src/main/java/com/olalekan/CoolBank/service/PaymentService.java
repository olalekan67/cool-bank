package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.response.IntialisePaymentResponse;

import java.math.BigDecimal;

public interface PaymentService {
     IntialisePaymentResponse initializePayment(BigDecimal amount);
     BaseResponseDto verifyPayment(String reference);
}
