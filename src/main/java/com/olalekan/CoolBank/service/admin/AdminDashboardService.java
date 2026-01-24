package com.olalekan.CoolBank.service.admin;

import com.olalekan.CoolBank.Utils.ActiveStatus;
import com.olalekan.CoolBank.Utils.TransactionStatus;
import com.olalekan.CoolBank.model.dto.admin.AdminActionLogResponseDto;
import com.olalekan.CoolBank.model.dto.admin.AdminDashboardStatDto;
import com.olalekan.CoolBank.repo.AdminActionLogRepo;
import com.olalekan.CoolBank.repo.AppUserRepo;
import com.olalekan.CoolBank.repo.TransactionRepo;
import com.olalekan.CoolBank.repo.WalletRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {
    private final AppUserRepo userRepo;
    private final WalletRepo walletRepo;
    private final TransactionRepo transactionRepo;
    private final AdminActionLogRepo adminActionLogRepo;

    public AdminDashboardStatDto dashboardStat() {

        long totalUsers = userRepo.count();
        long activeUsers = userRepo.countByActiveStatus(ActiveStatus.ACTIVE);
        BigDecimal totalTransactionVolume = transactionRepo.sumSuccessfulTransaction(TransactionStatus.SUCCESS);
        long totalTransactionCount = transactionRepo.count();
        long pendingKyc = userRepo.countByActiveStatus(ActiveStatus.PENDING_VERIFICATION);

        return AdminDashboardStatDto.builder()
                .activeUser(activeUsers)
                .totalUsers(totalUsers)
                .totalTransactionCount(totalTransactionCount)
                .totalTransactionVolume(totalTransactionVolume)
                .pendingKycCount(pendingKyc)
                .build();
    }

    public Page<AdminActionLogResponseDto> auditLogs(Pageable pageable) {

        return adminActionLogRepo.findAll(pageable).map(auditLog -> (
                    AdminActionLogResponseDto.builder()
                            .id(auditLog.getId())
                            .adminName(auditLog.getAdmin().getFullName())
                            .action(auditLog.getAction().toString())
                            .reason(auditLog.getReason())
                            .targetId(auditLog.getTargetId())
                            .createdAt(auditLog.getCreatedAt())
                            .build()
                ));
    }
}
