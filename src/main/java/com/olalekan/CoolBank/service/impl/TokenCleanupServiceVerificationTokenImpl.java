package com.olalekan.CoolBank.service.impl;

import com.olalekan.CoolBank.repo.TokenRepo;
import com.olalekan.CoolBank.service.TokenCleanupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenCleanupServiceVerificationTokenImpl implements TokenCleanupService {

    private final TokenRepo tokenRepo;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void removedExpiredToken(){

        LocalDateTime now = LocalDateTime.now();

        int deletedCount = tokenRepo.deleteByExpiredAtBefore(now);

        System.out.println("Cleanup job executed. Deleted " + deletedCount + " expired tokens.");
    }
}
