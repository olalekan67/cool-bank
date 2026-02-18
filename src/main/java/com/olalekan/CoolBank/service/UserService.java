package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.model.dto.request.ForgotPasswordRequestDto;
import com.olalekan.CoolBank.model.dto.request.LoginRequestDto;
import com.olalekan.CoolBank.model.dto.request.RefreshTokenRequestDto;
import com.olalekan.CoolBank.model.dto.request.RegisterRequestDto;
import com.olalekan.CoolBank.model.dto.request.ResetPasswordRequestDto;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.response.LoginResponseDto;

public interface UserService {
    BaseResponseDto registerUsers(RegisterRequestDto registerRequestDto);

    BaseResponseDto verifyUsers(String userToken);

    LoginResponseDto login(LoginRequestDto requestDto);

    LoginResponseDto refreshToken(RefreshTokenRequestDto requestDto);

    BaseResponseDto forgotPassword(ForgotPasswordRequestDto input);

    BaseResponseDto resetPassword(ResetPasswordRequestDto input);
}
