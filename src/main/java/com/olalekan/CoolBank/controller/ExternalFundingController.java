package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.dto.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.IntialisePaymentResponse;
import com.olalekan.CoolBank.service.PaystackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class ExternalFundingController {

    private final PaystackService paystackService;

    @PostMapping("fund")
    public ResponseEntity<IntialisePaymentResponse> fund(@RequestBody BigDecimal amount){
        IntialisePaymentResponse response = paystackService.initializePayment(amount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("verify_payment/{reference}")
    public ResponseEntity<BaseResponseDto> verfyPayment(@PathVariable String reference){
        BaseResponseDto responseDto = paystackService.verifyPayment(reference);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
