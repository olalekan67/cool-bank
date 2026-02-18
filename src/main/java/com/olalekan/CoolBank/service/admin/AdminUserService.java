package com.olalekan.CoolBank.service.admin;

import com.olalekan.CoolBank.model.dto.admin.request.AccountKycUpgradeDto;
import com.olalekan.CoolBank.model.dto.admin.request.AdminFreezeUserDto;
import com.olalekan.CoolBank.model.dto.admin.response.UserSummaryBriefResponseDto;
import com.olalekan.CoolBank.model.dto.admin.response.UserSummaryResponseDto;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUserService {

    Page<UserSummaryBriefResponseDto> getUsers(Pageable pageable);
    UserSummaryResponseDto getUser(String userId);
    BaseResponseDto freezeUser(String userId, AdminFreezeUserDto adminFreezeUserDto);
    BaseResponseDto unFreezeUser(String userId, AdminFreezeUserDto adminFreezeUserDto);
    BaseResponseDto upgradeKyc(String userId, AccountKycUpgradeDto kycUpgradeDto);
}
