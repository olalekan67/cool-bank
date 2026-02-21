package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.model.dto.request.ForgotPasswordRequestDto;
import com.olalekan.CoolBank.model.dto.request.LoginRequestDto;
import com.olalekan.CoolBank.model.dto.request.RefreshTokenRequestDto;
import com.olalekan.CoolBank.model.dto.request.RegisterRequestDto;
import com.olalekan.CoolBank.model.dto.request.ResetPasswordRequestDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.response.LoginResponseDto;

public interface UserService {
    ApiResponse registerUsers(RegisterRequestDto registerRequestDto);

    ApiResponse verifyUsers(String userToken);

    ApiResponse login(LoginRequestDto requestDto);

    ApiResponse refreshToken(RefreshTokenRequestDto requestDto);

    ApiResponse forgotPassword(ForgotPasswordRequestDto input);

    ApiResponse resetPassword(ResetPasswordRequestDto input);
}
