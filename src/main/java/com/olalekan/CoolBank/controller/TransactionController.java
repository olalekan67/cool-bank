package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.dto.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.TransferRequestDto;
import com.olalekan.CoolBank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("transfer")
    public ResponseEntity<BaseResponseDto> transfer(@RequestBody @Valid TransferRequestDto requestDto){
        BaseResponseDto responseDto = transactionService.transfer(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
