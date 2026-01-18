package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.dto.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.TransactionResponseDto;
import com.olalekan.CoolBank.model.dto.TransferRequestDto;
import com.olalekan.CoolBank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("transfer")
    public ResponseEntity<BaseResponseDto> transfer(@RequestBody @Valid TransferRequestDto requestDto){
        BaseResponseDto responseDto = transactionService.transfer(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> transaction(@PathVariable("id") @Valid UUID id){
        TransactionResponseDto responseDto = transactionService.transaction(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("history")
    public ResponseEntity<List<TransactionResponseDto>> transactions(){
        List<TransactionResponseDto> transactionResponseDtoList = transactionService.history();
        return new ResponseEntity<>(transactionResponseDtoList, HttpStatus.OK);
    }
}
