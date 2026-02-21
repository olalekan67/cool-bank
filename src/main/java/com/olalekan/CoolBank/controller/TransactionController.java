package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.dto.request.TransferRequestDto;
import com.olalekan.CoolBank.model.dto.request.WithdrawalRequestDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse> transfer(@RequestBody @Valid TransferRequestDto requestDto){
//        BaseResponseDto responseDto = transactionService.transfer(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.transfer(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> transactions(@PathVariable("id") @Valid UUID id){
//        TransactionResponseDto responseDto = transactionService.getTransaction(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.getTransaction(id));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse> withdrawals(@RequestBody @Valid WithdrawalRequestDto input){
//        BaseResponseDto response = transactionService.withdraw(input);
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.withdraw(input));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse> transactionsHistory(){
//        List<TransactionResponseDto> transactionResponseDtoList = transactionService.histories();
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.histories());
    }
}
