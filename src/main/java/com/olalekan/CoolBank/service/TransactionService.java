package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.model.dto.request.TransferRequestDto;
import com.olalekan.CoolBank.model.dto.request.WithdrawalRequestDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.response.TransactionResponseDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    ApiResponse transfer(@Valid TransferRequestDto requestDto);
    ApiResponse getTransaction(@Valid UUID id);
    ApiResponse histories();
    ApiResponse withdraw(WithdrawalRequestDto input);
}
