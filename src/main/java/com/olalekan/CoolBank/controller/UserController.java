package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.BaseEntity;
import com.olalekan.CoolBank.model.dto.*;
import com.olalekan.CoolBank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class UserController {
    private final UserService userService;


    @PostMapping("register")
    public ResponseEntity<BaseResponseDto> register(@RequestBody @Valid RegisterRequestDto registerRequestDto){
        BaseResponseDto registerResponseDto = userService.register(registerRequestDto);
        return new ResponseEntity<>(registerResponseDto, HttpStatus.CREATED);

    }

    @PostMapping("verify")
    public ResponseEntity<BaseResponseDto> verify(@RequestBody @Valid VerifyTokenRequestDto verifyTokenRequestDto){
        String userToken = verifyTokenRequestDto.token();
        BaseResponseDto tokenResponse = userService.verify(userToken);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto){
        LoginResponseDto responseDto = userService.login(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("refreshToken")
    public ResponseEntity<LoginResponseDto> refreshToken(@RequestBody @Valid RefreshTokenRequestDto requestDto){
        LoginResponseDto responseDto = userService.refreshToken(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("forgotPassword")
    public ResponseEntity<BaseResponseDto> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDto input){
        BaseResponseDto response = userService.forgotPassword(input);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("resetPassword")
    public ResponseEntity<BaseResponseDto> resetPassword(@RequestBody @Valid ResetPasswordRequestDto input){
        BaseResponseDto response = userService.resetPassword(input);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
