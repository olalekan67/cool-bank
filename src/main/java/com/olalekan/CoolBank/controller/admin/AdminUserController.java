package com.olalekan.CoolBank.controller.admin;

import com.olalekan.CoolBank.model.dto.admin.request.AccountKycUpgradeDto;
import com.olalekan.CoolBank.model.dto.admin.request.AdminFreezeUserDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import com.olalekan.CoolBank.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin")
@Secured("ROLE_ADMIN")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("users")
    public ResponseEntity<ApiResponse> users(Pageable pageable){
//        Page<UserSummaryBriefResponseDto> users = adminUserService.getUsers(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminUserService.getUsers(pageable));
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<ApiResponse> user(@PathVariable String userId){
//        UserSummaryResponseDto user = adminUserService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminUserService.getUser(userId));
    }

    @PutMapping("users/{userId}/freeze")
    public ResponseEntity<ApiResponse> freezeUsers(@PathVariable String userId, @RequestBody AdminFreezeUserDto adminFreezeUserDto){
//        BaseResponseDto response = adminUserService.freezeUser(userId, adminFreezeUserDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminUserService.freezeUser(userId, adminFreezeUserDto));
    }

    @PutMapping("users/{userId}/unfreeze")
    public ResponseEntity<ApiResponse> unfreezeUsers(@PathVariable String userId, @RequestBody AdminFreezeUserDto adminFreezeUserDto){
//        BaseResponseDto response = adminUserService.unFreezeUser(userId, adminFreezeUserDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminUserService.unFreezeUser(userId, adminFreezeUserDto));
    }

    @PutMapping("users/{userId}/kyc")
    public ResponseEntity<ApiResponse> kycUpgrades(@PathVariable String userId, @RequestBody AccountKycUpgradeDto kycUpgradeDto){
//        BaseResponseDto responseDto = adminUserService.upgradeKyc(userId, kycUpgradeDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminUserService.upgradeKyc(userId, kycUpgradeDto));
    }

}
