package com.olalekan.CoolBank.service.admin;

import com.olalekan.CoolBank.exception.TransactionNotFoundException;
import com.olalekan.CoolBank.model.Transaction;
import com.olalekan.CoolBank.model.dto.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.TransactionResponseDto;
import com.olalekan.CoolBank.model.dto.admin.AdminTransactionAdjustmentDto;
import com.olalekan.CoolBank.model.dto.admin.AdminTransactionResponseBrief;
import com.olalekan.CoolBank.repo.AdminActionLogRepo;
import com.olalekan.CoolBank.repo.AppUserRepo;
import com.olalekan.CoolBank.repo.TransactionRepo;
import com.olalekan.CoolBank.repo.WalletRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminTransactionService {

    private final AdminActionLogRepo adminActionLogRepo;
    private final TransactionRepo transactionRepo;
    private final WalletRepo walletRepo;
    private final AppUserRepo userRepo;

    public Page<AdminTransactionResponseBrief> transactions(Pageable pageable) {

        return transactionRepo.findAll(pageable).map(transaction -> (
           AdminTransactionResponseBrief.builder()
                   .senderEmail(transaction.getExternalReference() == null ? transaction.getSourceWallet().getUser().getEmail() : "External Funding")
                   .senderFullName(transaction.getExternalReference() == null ? transaction.getSourceWallet().getUser().getFullName() : "Paystack funding")
                   .amount(transaction.getAmount())
                   .receiverEmail(transaction.getDestinationWallet().getUser().getEmail())
                   .receiverFullName(transaction.getDestinationWallet().getUser().getFullName())
                   .reference(transaction.getReference())
                   .dateTime(transaction.getCreatedAt())
                   .externalReference(transaction.getExternalReference())
                   .build()
        ));
    }

    public TransactionResponseDto transaction(String reference) {

        Transaction transaction = transactionRepo.findByReference(reference)
                .orElseThrow(() -> new TransactionNotFoundException("This transaction does not exist"));

        return TransactionResponseDto.builder()
                .senderEmail(transaction.getExternalReference() == null ? transaction.getSourceWallet().getUser().getEmail() : "External Funding Via paystack")
                .senderFullName(transaction.getExternalReference() == null ? transaction.getSourceWallet().getUser().getFullName() : "Paystack Funding")
                .receiverEmail(transaction.getDestinationWallet().getUser().getEmail())
                .receiverFullName(transaction.getDestinationWallet().getUser().getFullName())
                .type(transaction.getType())
                .status(transaction.getStatus())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .reference(transaction.getReference())
                .externalReference(transaction.getExternalReference())
                .build();
    }

    public BaseResponseDto credit(AdminTransactionAdjustmentDto adjustmentDto) {
        return null;
    }
}
