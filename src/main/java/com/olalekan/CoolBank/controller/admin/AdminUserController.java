package com.olalekan.CoolBank.controller.admin;

import com.olalekan.CoolBank.model.dto.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.admin.AccountKycUpgradeDto;
import com.olalekan.CoolBank.model.dto.admin.AdminFreezeUserDto;
import com.olalekan.CoolBank.model.dto.admin.UserSummaryBriefResponseDto;
import com.olalekan.CoolBank.model.dto.admin.UserSummaryResponseDto;
import com.olalekan.CoolBank.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin")
@Secured("ROLE_ADMIN")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("users")
    public ResponseEntity<Page<UserSummaryBriefResponseDto>> users(Pageable pageable){
        Page<UserSummaryBriefResponseDto> users = adminUserService.getUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<UserSummaryResponseDto> user(@PathVariable String userId){

        UserSummaryResponseDto user = adminUserService.getUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("users/{userId}/freeze")
    public ResponseEntity<BaseResponseDto> freezeUser(@PathVariable String userId, @RequestBody AdminFreezeUserDto adminFreezeUserDto){
        BaseResponseDto response = adminUserService.freezeUser(userId, adminFreezeUserDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("users/{userId}/unfreeze")
    public ResponseEntity<BaseResponseDto> unFreezeUser(@PathVariable String userId, @RequestBody AdminFreezeUserDto adminFreezeUserDto){
        BaseResponseDto response = adminUserService.unFreezeUser(userId, adminFreezeUserDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("users/{userId}/kyc")
    public ResponseEntity<BaseResponseDto> kycUpgrade(@PathVariable String userId, @RequestBody AccountKycUpgradeDto kycUpgradeDto){
        BaseResponseDto responseDto = adminUserService.upgradeKyc(userId, kycUpgradeDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
