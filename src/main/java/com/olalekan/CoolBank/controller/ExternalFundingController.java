package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.dto.request.FundRequestDto;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.response.IntialisePaymentResponse;
import com.olalekan.CoolBank.service.PaystackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("api/v1")
public class ExternalFundingController {

    private final PaystackService paystackService;

    @PostMapping("fund")
    public ResponseEntity<IntialisePaymentResponse> fundAccounts(@RequestBody @Valid FundRequestDto requestDto){
        IntialisePaymentResponse response = paystackService.initializePayment(BigDecimal.valueOf(requestDto.amount()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("verifyPayment")
    public ResponseEntity<BaseResponseDto> verifyPayments(@RequestParam("reference") String reference){
        BaseResponseDto responseDto = paystackService.verifyPayment(reference);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
