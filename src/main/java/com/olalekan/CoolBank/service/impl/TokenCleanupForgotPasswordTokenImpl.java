package com.olalekan.CoolBank.service.impl;

import com.olalekan.CoolBank.repo.ForgotPasswordTokenRepo;
import com.olalekan.CoolBank.service.TokenCleanupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenCleanupForgotPasswordTokenImpl implements TokenCleanupService {
    private final ForgotPasswordTokenRepo forgotPasswordTokenRepo;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void removedExpiredToken(){

        LocalDateTime now = LocalDateTime.now();

        int deletedCount = forgotPasswordTokenRepo.deleteByExpiresAtBefore(now);

        System.out.println("Cleanup job executed. Deleted " + deletedCount + " forgot password expired tokens.");
    }
}
