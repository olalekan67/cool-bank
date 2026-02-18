package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.model.dto.request.TransferRequestDto;
import com.olalekan.CoolBank.model.dto.request.WithdrawalRequestDto;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.response.TransactionResponseDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    BaseResponseDto transfer(@Valid TransferRequestDto requestDto);
    TransactionResponseDto getTransaction(@Valid UUID id);
    List<TransactionResponseDto> histories();
    BaseResponseDto withdraw(WithdrawalRequestDto input);
}
