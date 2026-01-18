package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.dto.*;
import com.olalekan.CoolBank.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class WalletController {
    public final WalletService walletService;

    @PostMapping("pin")
    public ResponseEntity<BaseResponseDto> createPin(@RequestBody @Valid CreatePinRequestDto requestDto){
        BaseResponseDto responseDto = walletService.createPin(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("pin")
    public ResponseEntity<BaseResponseDto> changePin(@RequestBody @Valid UpdatePinRequestDto requestDto){
        BaseResponseDto responseDto = walletService.changePin(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("balance")
    public ResponseEntity<BalanceResponseDto> balance(){
        BalanceResponseDto responseDto = walletService.balance();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
