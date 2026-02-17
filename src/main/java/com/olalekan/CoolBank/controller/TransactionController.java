package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.dto.request.TransferRequestDto;
import com.olalekan.CoolBank.model.dto.request.WithdrawalRequestDto;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.response.TransactionResponseDto;
import com.olalekan.CoolBank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("transfer")
    public ResponseEntity<BaseResponseDto> transfers(@RequestBody @Valid TransferRequestDto requestDto){
        BaseResponseDto responseDto = transactionService.transfer(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> transaction(@PathVariable("id") @Valid UUID id){
        TransactionResponseDto responseDto = transactionService.transaction(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<BaseResponseDto> withdrawals(@RequestBody @Valid WithdrawalRequestDto input){
        BaseResponseDto response = transactionService.withdraw(input);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("history")
    public ResponseEntity<List<TransactionResponseDto>> transactions(){
        List<TransactionResponseDto> transactionResponseDtoList = transactionService.histories();
        return new ResponseEntity<>(transactionResponseDtoList, HttpStatus.OK);
    }
}
