package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.dto.request.ForgotPasswordRequestDto;
import com.olalekan.CoolBank.model.dto.request.LoginRequestDto;
import com.olalekan.CoolBank.model.dto.request.RefreshTokenRequestDto;
import com.olalekan.CoolBank.model.dto.request.RegisterRequestDto;
import com.olalekan.CoolBank.model.dto.request.ResetPasswordRequestDto;
import com.olalekan.CoolBank.model.dto.request.VerifyTokenRequestDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import com.olalekan.CoolBank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class UserController {
    private final UserService userService;


    @PostMapping("register")
    public ResponseEntity<ApiResponse> registerUsers(@RequestBody @Valid RegisterRequestDto registerRequestDto){
//        BaseResponseDto registerResponseDto = userService.registerUsers(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registerUsers(registerRequestDto));
    }

    @PostMapping("verify")
    public ResponseEntity<ApiResponse> verifyUsers(@RequestBody @Valid VerifyTokenRequestDto verifyTokenRequestDto){
        String userToken = verifyTokenRequestDto.token();
//        BaseResponseDto tokenResponse = userService.verifyUsers(userToken);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.verifyUsers(userToken));
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginRequestDto requestDto){
//        LoginResponseDto responseDto = userService.login(requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.login(requestDto));
    }

    @PostMapping("refreshToken")
    public ResponseEntity<ApiResponse> refreshTokens(@RequestBody @Valid RefreshTokenRequestDto requestDto){
//        LoginResponseDto responseDto = userService.refreshToken(requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.refreshToken(requestDto));
    }

    @PostMapping("forgotPassword")
    public ResponseEntity<ApiResponse> forgotPasswords(@RequestBody @Valid ForgotPasswordRequestDto input){
//        BaseResponseDto response = userService.forgotPassword(input);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.forgotPassword(input));
    }

    @PostMapping("resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody @Valid ResetPasswordRequestDto input){
//        BaseResponseDto response = userService.resetPassword(input);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.resetPassword(input));
    }

}
