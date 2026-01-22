package com.olalekan.CoolBank.service.admin;

import com.olalekan.CoolBank.Utils.ActiveStatus;
import com.olalekan.CoolBank.Utils.AdminActionType;
import com.olalekan.CoolBank.Utils.UserRole;
import com.olalekan.CoolBank.exception.UserNotFoundException;
import com.olalekan.CoolBank.model.AdminActionLog;
import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.dto.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.admin.AccountKycUpgradeDto;
import com.olalekan.CoolBank.model.dto.admin.AdminFreezeUserDto;
import com.olalekan.CoolBank.model.dto.admin.UserSummaryBriefResponseDto;
import com.olalekan.CoolBank.model.dto.admin.UserSummaryResponseDto;
import com.olalekan.CoolBank.repo.AdminActionLogRepo;
import com.olalekan.CoolBank.repo.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AppUserRepo userRepo;
    private final AdminActionLogRepo adminActionLogRepo;

    public Page<UserSummaryBriefResponseDto> getUsers(Pageable pageable) {

        return userRepo.findAll(pageable)
                .map(user -> (
                        UserSummaryBriefResponseDto.builder()
                         .userId(user.getId())
                         .email(user.getEmail())
                         .kycTier(user.getKycTier().toString())
                         .phoneNumber(user.getPhoneNumber())
                         .fullName(user.getFullName())
                        .build()
                ));

    }

    public UserSummaryResponseDto getUser(String userId) {

        AppUser user = userRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        return UserSummaryResponseDto.builder()
                .activeStatus(user.getActiveStatus().toString())
                .createdAt(user.getCreatedAt())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .kycTier(user.getKycTier().toString())
                .phoneNumber(user.getPhoneNumber())
                .userId(user.getId())
                .walletBalance(user.getWallet().getBalance())
                .build();
    }


    @Transactional
    public BaseResponseDto freezeUser(String userId, AdminFreezeUserDto adminFreezeUserDto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser adminUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("This admin does not exist"));

        AppUser user = userRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        if(user.getRole() == UserRole.ADMIN){
            return BaseResponseDto.builder()
                    .message("You cannot freeze an admin account")
                    .build();
        }

        if(user.getActiveStatus() == ActiveStatus.BANNED){
            return BaseResponseDto.builder()
                    .message("This user account has already been frozen")
                    .build();
        }

        if(user.getActiveStatus() == ActiveStatus.PENDING_VERIFICATION){
            return BaseResponseDto.builder()
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
        return BaseResponseDto.builder()
                .message("User account with email: " + user.getEmail() + ", has been frozen successfully")
                .build();
    }

    @Transactional
    public BaseResponseDto unFreezeUser(String userId, AdminFreezeUserDto adminFreezeUserDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser adminUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("This admin does not exist"));

        AppUser user = userRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        if(user.getActiveStatus() == ActiveStatus.ACTIVE){
            return BaseResponseDto.builder()
                    .message("This user account is already active")
                    .build();
        }

        if(user.getActiveStatus() == ActiveStatus.PENDING_VERIFICATION){
            return BaseResponseDto.builder()
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
        return BaseResponseDto.builder()
                .message("User account with email: " + user.getEmail() + ", has been unfrozen successfully")
                .build();
    }

    @Transactional
    public BaseResponseDto upgradeKyc(String userId, AccountKycUpgradeDto kycUpgradeDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser adminUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        AppUser user = userRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        if(user.getActiveStatus() == ActiveStatus.PENDING_VERIFICATION){
            return BaseResponseDto.builder()
                    .message("Please verify your email first before you proceed")
                    .build();
        }

        if(user.getActiveStatus() == ActiveStatus.SUSPENDED){
            return BaseResponseDto.builder()
                    .message("This user account has been frozen, please resolve the issue before you proceed")
                    .build();
        }

        int comparison = user.getKycTier().compareTo(kycUpgradeDto.tier());


        if(comparison >= 0){
            return BaseResponseDto.builder()
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
        return BaseResponseDto.builder()
                .message("This user has been successfully upgraded to " + user.getKycTier())
                .build();
    }
}
