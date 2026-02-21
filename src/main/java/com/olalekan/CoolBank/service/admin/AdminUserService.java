package com.olalekan.CoolBank.service.admin;

import com.olalekan.CoolBank.model.dto.admin.request.AccountKycUpgradeDto;
import com.olalekan.CoolBank.model.dto.admin.request.AdminFreezeUserDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface AdminUserService {

    ApiResponse getUsers(Pageable pageable);
    ApiResponse getUser(String userId);
    ApiResponse freezeUser(String userId, AdminFreezeUserDto adminFreezeUserDto);
    ApiResponse unFreezeUser(String userId, AdminFreezeUserDto adminFreezeUserDto);
    ApiResponse upgradeKyc(String userId, AccountKycUpgradeDto kycUpgradeDto);
}
