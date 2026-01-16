package com.olalekan.CoolBank.controller;

import com.olalekan.CoolBank.model.dto.RegisterRequestDto;
import com.olalekan.CoolBank.model.dto.RegisterResponseDto;
import com.olalekan.CoolBank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class UserController {
    private UserService userService;


    @PostMapping("register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto registerRequestDto){
        RegisterResponseDto registerResponseDto = userService.register(registerRequestDto);
        return new ResponseEntity<>(registerResponseDto, HttpStatus.CREATED);

    }

}
