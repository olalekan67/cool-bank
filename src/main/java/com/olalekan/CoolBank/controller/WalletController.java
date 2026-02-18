package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.dto.request.CreatePinRequestDto;
import com.olalekan.CoolBank.model.dto.request.UpdatePinRequestDto;
import com.olalekan.CoolBank.model.dto.response.BalanceResponseDto;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/wallets")
public class WalletController {
    public final WalletService walletService;

    @PostMapping("pin")
    public ResponseEntity<BaseResponseDto> createPins(@RequestBody @Valid CreatePinRequestDto requestDto){
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
        BalanceResponseDto responseDto = walletService.getBalance();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
