package com.olalekan.CoolBank.service.admin;

import com.olalekan.CoolBank.Utils.AdminActionType;
import com.olalekan.CoolBank.Utils.TransactionStatus;
import com.olalekan.CoolBank.Utils.TransactionType;
import com.olalekan.CoolBank.exception.InsufficientBalanceException;
import com.olalekan.CoolBank.exception.TransactionNotFoundException;
import com.olalekan.CoolBank.exception.UserNotFoundException;
import com.olalekan.CoolBank.model.AdminActionLog;
import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.Transaction;
import com.olalekan.CoolBank.model.Wallet;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminTransactionService {

    private final AdminActionLogRepo adminActionLogRepo;
    private final TransactionRepo transactionRepo;
    private final WalletRepo walletRepo;
    private final AppUserRepo userRepo;

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional
    public BaseResponseDto credit(AdminTransactionAdjustmentDto adjustmentDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser adminUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("This user does not exist"));

        AppUser user = userRepo.findByEmail(adjustmentDto.email())
                .orElseThrow(() -> new UserNotFoundException("This user does not exist"));

        Wallet userWallet = walletRepo.findByIdWithLock(user.getWallet().getId())
                .orElseThrow(() -> new BadCredentialsException("Wallet does not exist"));
        userWallet.setBalance(userWallet.getBalance().add(adjustmentDto.amount()));
        String externalReference = UUID.randomUUID().toString();
        String internalReference = UUID.randomUUID().toString();

        Transaction transaction = Transaction.builder()
                .destinationWallet(userWallet)
                .description(adjustmentDto.description())
                .type(TransactionType.FUNDING)
                .amount(adjustmentDto.amount())
                .externalReference(externalReference)
                .status(TransactionStatus.SUCCESS)
                .reference(internalReference)
                .description(adjustmentDto.description())
                .build();

        AdminActionLog adminAction = AdminActionLog.builder()
                .action(AdminActionType.CREDIT_WALLET)
                .admin(adminUser)
                .reason(adjustmentDto.reason())
                .targetId(adjustmentDto.email())
                .build();

        walletRepo.save(userWallet);
        transactionRepo.save(transaction);
        adminActionLogRepo.save(adminAction);
        return BaseResponseDto.builder()
                .message("User account with email: " + adjustmentDto.email() + " has been credited " + adjustmentDto.amount() + " Successfully" )
                .build();
    }


    @Transactional
    public BaseResponseDto debit(AdminTransactionAdjustmentDto adjustmentDto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser adminUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        AppUser user = userRepo.findByEmail(adjustmentDto.email())
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        Wallet userWallet = walletRepo.findByIdWithLock(user.getWallet().getId())
                .orElseThrow(() -> new BadCredentialsException("Wallet does not exist"));

        boolean comparison = userWallet.getBalance().compareTo(adjustmentDto.amount()) > 0;
        if(comparison){
            throw new InsufficientBalanceException("The user balance is less than the amount intended for deduction");
        }

        userWallet.setBalance(userWallet.getBalance().subtract(adjustmentDto.amount()));
        String externalReference = UUID.randomUUID().toString();
        String internalReference = UUID.randomUUID().toString();

        Transaction transaction = Transaction.builder()
                .sourceWallet(userWallet)
                .description(adjustmentDto.description())
                .type(TransactionType.WITHDRAWAL)
                .amount(adjustmentDto.amount())
                .externalReference(externalReference)
                .status(TransactionStatus.SUCCESS)
                .reference(internalReference)
                .description(adjustmentDto.description())
                .build();

        AdminActionLog adminAction = AdminActionLog.builder()
                .action(AdminActionType.DEBIT_WALLET)
                .admin(adminUser)
                .reason(adjustmentDto.reason())
                .targetId(adjustmentDto.email())
                .build();

        walletRepo.save(userWallet);
        transactionRepo.save(transaction);
        adminActionLogRepo.save(adminAction);
        return BaseResponseDto.builder()
                .message("User account with email: " + adjustmentDto.email() + " has been debited " + adjustmentDto.amount() + " Successfully" )
                .build();
    }
}
