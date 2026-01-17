package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.dto.BalanceResponseDto;
import com.olalekan.CoolBank.model.dto.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.CreatePinRequestDto;
import com.olalekan.CoolBank.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("balance")
    public ResponseEntity<BalanceResponseDto> balance(){
        BalanceResponseDto responseDto = walletService.balance();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
