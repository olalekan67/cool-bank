package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.Utils.TransactionStatus;
import com.olalekan.CoolBank.model.Transaction;
import com.olalekan.CoolBank.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionCleanupService {
    private final TransactionRepo transactionRepo;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void changePendingPaymentToFailed(){

        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(10);

        List<Transaction> transactions = transactionRepo.findByStatusAndCreatedAtBefore(TransactionStatus.PENDING, cutoff);

        if(transactions.isEmpty()){
            return;
        }

        for (Transaction transaction : transactions){
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setDescription("Transaction timeout auto cancel");
        }
        transactionRepo.saveAll(transactions);
        System.out.println("Cleanup job executed. Updated " + transactions.size() + " pending transactions.");
    }

}
