package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.dto.request.FundRequestDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import com.olalekan.CoolBank.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/funding")
public class ExternalFundingController {

    private final PaymentService paymentService;

    @PostMapping("fund")
    public ResponseEntity<ApiResponse> fundAccounts(@RequestBody @Valid FundRequestDto requestDto){
        return ResponseEntity.ok(paymentService.initializePayment(BigDecimal.valueOf(requestDto.amount())));
    }

    @GetMapping("verifyPayment")
    public ResponseEntity<ApiResponse> verifyPayments(@RequestParam("reference") String reference){
        return ResponseEntity.ok(paymentService.verifyPayment(reference));
    }
}
