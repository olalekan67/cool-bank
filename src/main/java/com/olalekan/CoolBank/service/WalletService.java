package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.model.dto.request.CreatePinRequestDto;
import com.olalekan.CoolBank.model.dto.request.UpdatePinRequestDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;

public interface WalletService {
    ApiResponse createPin(CreatePinRequestDto requestDto);
    ApiResponse getBalance();
    ApiResponse changePin(UpdatePinRequestDto requestDto);
}
