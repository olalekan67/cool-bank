package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.response.IntialisePaymentResponse;

import java.math.BigDecimal;

public interface PaymentService {
     ApiResponse initializePayment(BigDecimal amount);
     ApiResponse verifyPayment(String reference);
}
