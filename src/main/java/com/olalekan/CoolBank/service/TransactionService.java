package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.Utils.TransactionStatus;
import com.olalekan.CoolBank.Utils.TransactionType;
import com.olalekan.CoolBank.exception.DuplicateTransactionException;
import com.olalekan.CoolBank.exception.InsufficientBalanceException;
import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.Transaction;
import com.olalekan.CoolBank.model.Wallet;
import com.olalekan.CoolBank.model.dto.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.TransferRequestDto;
import com.olalekan.CoolBank.repo.AppUserRepo;
import com.olalekan.CoolBank.repo.TransactionRepo;
import com.olalekan.CoolBank.repo.WalletRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepo transactionRepo;
    private final WalletRepo walletRepo;
    private final AppUserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public BaseResponseDto transfer(@Valid TransferRequestDto requestDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(transactionRepo.existsByReference(requestDto.reference())){
            throw new DuplicateTransactionException("Transaction Already processed");
        }
        AppUser senderUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        Wallet senderWallet = walletRepo.findByIdWithLock(senderUser.getWallet().getId())
                .orElseThrow(() -> new NoSuchElementException("Invalid user"));

        if((senderWallet.getBalance().compareTo(requestDto.amount())) < 0){
            throw new InsufficientBalanceException("Insufficient balance");
        }

        if(!(passwordEncoder.matches(requestDto.pin(), senderWallet.getPin()))){
            throw new BadCredentialsException("Incorrect Pin");
        }

        AppUser receiverUser = userRepo.findByEmail(requestDto.recipientEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid recipient email"));

        Wallet receiverWallet = receiverUser.getWallet();

        if(senderWallet.getId().equals(receiverWallet.getId())){
            throw new IllegalArgumentException("Cannot transfer to self");
        }


        senderWallet.setBalance(senderWallet.getBalance().subtract(requestDto.amount()));
        receiverWallet.setBalance(receiverWallet.getBalance().add(requestDto.amount()));

        walletRepo.save(senderWallet);
        walletRepo.save(receiverWallet);

        Transaction transaction = Transaction.builder()
                .amount(requestDto.amount())
                .description(requestDto.description())
                .sourceWallet(senderWallet)
                .destinationWallet(receiverWallet)
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.SUCCESS)
                .reference(requestDto.reference())
                .build();

        transactionRepo.save(transaction);

        return BaseResponseDto.builder()
                .message("Transfer done successfully")
                .build();
    }
}
