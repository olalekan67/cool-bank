package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.model.dto.request.CreatePinRequestDto;
import com.olalekan.CoolBank.model.dto.request.UpdatePinRequestDto;
import com.olalekan.CoolBank.model.dto.response.BalanceResponseDto;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;

public interface WalletService {
    BaseResponseDto createPin(CreatePinRequestDto requestDto);
    BalanceResponseDto getBalance();
    BaseResponseDto changePin(UpdatePinRequestDto requestDto);
}
