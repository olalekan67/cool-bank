package com.olalekan.CoolBank.service.admin.impl;

import com.olalekan.CoolBank.Utils.ActiveStatus;
import com.olalekan.CoolBank.Utils.AdminActionType;
import com.olalekan.CoolBank.Utils.UserRole;
import com.olalekan.CoolBank.exception.UserNotFoundException;
import com.olalekan.CoolBank.model.AdminActionLog;
import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.dto.admin.request.AccountKycUpgradeDto;
import com.olalekan.CoolBank.model.dto.admin.request.AdminFreezeUserDto;
import com.olalekan.CoolBank.model.dto.admin.response.UserSummaryBriefResponseDto;
import com.olalekan.CoolBank.model.dto.admin.response.UserSummaryResponseDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.repo.AdminActionLogRepo;
import com.olalekan.CoolBank.repo.AppUserRepo;
import com.olalekan.CoolBank.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final AppUserRepo userRepo;
    private final AdminActionLogRepo adminActionLogRepo;

    public ApiResponse getUsers(Pageable pageable) {

        Page<UserSummaryBriefResponseDto> users = userRepo.findAll(pageable)
                .map(user -> (
                        UserSummaryBriefResponseDto.builder()
                                .userId(user.getId())
                                .email(user.getEmail())
                                .kycTier(user.getKycTier().toString())
                                .phoneNumber(user.getPhoneNumber())
                                .fullName(user.getFullName())
                                .build()
                ));

        return ApiResponse.builder()
                        .error(false)
                        .message("All Users")
                        .data(users)
                        .build();

    }

    public ApiResponse getUser(String userId) {

        AppUser user = userRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        UserSummaryResponseDto userSummary = UserSummaryResponseDto.builder()
                .activeStatus(user.getActiveStatus().toString())
                .createdAt(user.getCreatedAt())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .kycTier(user.getKycTier().toString())
                .phoneNumber(user.getPhoneNumber())
                .userId(user.getId())
                .walletBalance(user.getWallet().getBalance())
                .build();

        return ApiResponse.builder()
                .error(false)
                .message("Get single User")
                .data(userSummary)
                .build();
    }


    @Transactional
    public ApiResponse freezeUser(String userId, AdminFreezeUserDto adminFreezeUserDto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser adminUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("This admin does not exist"));

        AppUser user = userRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        if(user.getRole() == UserRole.ADMIN){
            return ApiResponse.builder()
                    .error(true)
                    .message("You cannot freeze an admin account")
                    .build();
        }

        if(user.getActiveStatus() == ActiveStatus.BANNED){
            return ApiResponse.builder()
                    .error(true)
                    .message("This user account has already been frozen")
                    .build();
        }

        if(user.getActiveStatus() == ActiveStatus.PENDING_VERIFICATION){
            return ApiResponse.builder()
                    .error(true)
                    .message("This user has not verified his account")
                    .build();
        }

        user.setActiveStatus(ActiveStatus.SUSPENDED);
        AdminActionLog adminAction = AdminActionLog.builder()
                .admin(adminUser)
                .action(AdminActionType.FREEZE_USER)
                .reason(adminFreezeUserDto.reason())
                .targetId(user.getEmail())
                .build();

        userRepo.save(user);
        adminActionLogRepo.save(adminAction);
        return ApiResponse.builder()
                .error(false)
                .message("User account with email: " + user.getEmail() + ", has been frozen successfully")
                .data(user)
                .build();
    }

    @Transactional
    public ApiResponse unFreezeUser(String userId, AdminFreezeUserDto adminFreezeUserDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser adminUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("This admin does not exist"));

        AppUser user = userRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        if(user.getActiveStatus() == ActiveStatus.ACTIVE){
            return ApiResponse.builder()
                    .error(false)
                    .message("This user account is already active")
                    .build();
        }

        if(user.getActiveStatus() == ActiveStatus.PENDING_VERIFICATION){
            return ApiResponse.builder()
                    .error(false)
                    .message("This user has not verified his account")
                    .build();
        }

        user.setActiveStatus(ActiveStatus.ACTIVE);
        AdminActionLog adminAction = AdminActionLog.builder()
                .admin(adminUser)
                .action(AdminActionType.UNFREEZE_USER)
                .reason(adminFreezeUserDto.reason())
                .targetId(user.getEmail())
                .build();

        userRepo.save(user);
        adminActionLogRepo.save(adminAction);
        return ApiResponse.builder()
                .error(false)
                .message("User account with email: " + user.getEmail() + ", has been unfrozen successfully")
                .data(user)
                .build();
    }

    @Transactional
    public ApiResponse upgradeKyc(String userId, AccountKycUpgradeDto kycUpgradeDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser adminUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        AppUser user = userRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        if(user.getActiveStatus() == ActiveStatus.PENDING_VERIFICATION){
            return ApiResponse.builder()
                    .error(true)
                    .message("Please verify your email first before you proceed")
                    .build();
        }

        if(user.getActiveStatus() == ActiveStatus.SUSPENDED){
            return ApiResponse.builder()
                    .error(true)
                    .message("This user account has been frozen, please resolve the issue before you proceed")
                    .build();
        }

        int comparison = user.getKycTier().compareTo(kycUpgradeDto.tier());


        if(comparison >= 0){
            return ApiResponse.builder()
                    .error(true)
                    .message("New KYC tier must be greater than old tier")
                    .build();
        }
        user.setKycTier(kycUpgradeDto.tier());

        AdminActionLog adminAction = AdminActionLog.builder()
                .admin(adminUser)
                .action(AdminActionType.UPGRADE_KYC_TIER)
                .targetId(user.getEmail())
                .reason(kycUpgradeDto.reason())
                .build();

        userRepo.save(user);
        adminActionLogRepo.save(adminAction);
        return ApiResponse.builder()
                .error(false)
                .message("This user has been successfully upgraded to " + user.getKycTier())
                .data(user)
                .build();
    }
}
