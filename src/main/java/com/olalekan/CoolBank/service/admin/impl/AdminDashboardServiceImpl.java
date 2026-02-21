package com.olalekan.CoolBank.service.admin.impl;

import com.olalekan.CoolBank.Utils.ActiveStatus;
import com.olalekan.CoolBank.Utils.TransactionStatus;
import com.olalekan.CoolBank.model.dto.admin.request.AdminDashboardStatDto;
import com.olalekan.CoolBank.model.dto.admin.response.AdminActionLogResponseDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import com.olalekan.CoolBank.repo.AdminActionLogRepo;
import com.olalekan.CoolBank.repo.AppUserRepo;
import com.olalekan.CoolBank.repo.TransactionRepo;
import com.olalekan.CoolBank.repo.WalletRepo;
import com.olalekan.CoolBank.service.admin.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {
    private final AppUserRepo userRepo;
    private final WalletRepo walletRepo;
    private final TransactionRepo transactionRepo;
    private final AdminActionLogRepo adminActionLogRepo;

    public ApiResponse dashboardStat() {

        long totalUsers = userRepo.count();
        long activeUsers = userRepo.countByActiveStatus(ActiveStatus.ACTIVE);
        BigDecimal totalTransactionVolume = transactionRepo.sumSuccessfulTransaction(TransactionStatus.SUCCESS);
        long totalTransactionCount = transactionRepo.count();
        long pendingKyc = userRepo.countByActiveStatus(ActiveStatus.PENDING_VERIFICATION);

        AdminDashboardStatDto adminDashboardStat =  AdminDashboardStatDto.builder()
                .activeUser(activeUsers)
                .totalUsers(totalUsers)
                .totalTransactionCount(totalTransactionCount)
                .totalTransactionVolume(totalTransactionVolume)
                .pendingKycCount(pendingKyc)
                .build();

        return ApiResponse.builder()
                .error(false)
                .message("Admin dashboard stat")
                .data(adminDashboardStat)
                .build();
    }

    public ApiResponse auditLogs(Pageable pageable) {

        Page<AdminActionLogResponseDto> adminActionLogs = adminActionLogRepo.findAll(pageable).map(auditLog -> (
                AdminActionLogResponseDto.builder()
                        .id(auditLog.getId())
                        .adminName(auditLog.getAdmin().getFullName())
                        .action(auditLog.getAction().toString())
                        .reason(auditLog.getReason())
                        .targetId(auditLog.getTargetId())
                        .createdAt(auditLog.getCreatedAt())
                        .build()
        ));

        return ApiResponse.builder()
                .error(false)
                .message("Admin Actions log")
                .data(adminActionLogs)
                .build();
    }
}
