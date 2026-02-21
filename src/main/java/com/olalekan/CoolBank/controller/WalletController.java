package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.dto.request.CreatePinRequestDto;
import com.olalekan.CoolBank.model.dto.request.UpdatePinRequestDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
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
    public ResponseEntity<ApiResponse> createPins(@RequestBody @Valid CreatePinRequestDto requestDto){
//        BaseResponseDto responseDto = walletService.createPin(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(walletService.createPin(requestDto));
    }

    @PutMapping("pin")
    public ResponseEntity<ApiResponse> changePin(@RequestBody @Valid UpdatePinRequestDto requestDto){
//        BaseResponseDto responseDto = walletService.changePin(requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(walletService.changePin(requestDto));
    }

    @GetMapping("balance")
    public ResponseEntity<ApiResponse> balance(){
//        BalanceResponseDto responseDto = walletService.getBalance();
        return ResponseEntity.status(HttpStatus.OK)
                .body(walletService.getBalance());
    }
}
