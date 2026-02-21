package com.olalekan.CoolBank.service.impl;

import com.olalekan.CoolBank.Utils.ActiveStatus;
import com.olalekan.CoolBank.Utils.TransactionStatus;
import com.olalekan.CoolBank.Utils.TransactionType;
import com.olalekan.CoolBank.exception.DuplicateTransactionException;
import com.olalekan.CoolBank.exception.InsufficientBalanceException;
import com.olalekan.CoolBank.exception.InvalidUserStatusException;
import com.olalekan.CoolBank.exception.UnauthorizeUserException;
import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.Transaction;
import com.olalekan.CoolBank.model.Wallet;
import com.olalekan.CoolBank.model.dto.request.TransferRequestDto;
import com.olalekan.CoolBank.model.dto.request.WithdrawalRequestDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.response.TransactionResponseDto;
import com.olalekan.CoolBank.repo.AppUserRepo;
import com.olalekan.CoolBank.repo.TransactionRepo;
import com.olalekan.CoolBank.repo.WalletRepo;
import com.olalekan.CoolBank.service.TransactionService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;
    private final WalletRepo walletRepo;
    private final AppUserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public ApiResponse transfer(@Valid TransferRequestDto requestDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(transactionRepo.existsByReference(requestDto.reference())){
            throw new DuplicateTransactionException("Transaction Already processed");
        }
        AppUser senderUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        Wallet senderWallet = walletRepo.findByIdWithLock(senderUser.getWallet().getId())
                .orElseThrow(() -> new NoSuchElementException("Invalid user"));

        if(senderUser.getActiveStatus() == ActiveStatus.BANNED ||
                senderUser.getActiveStatus() == ActiveStatus.SUSPENDED){
            throw new InvalidUserStatusException("This account has been banned or suspended. kindly reach out to our customer service to resolve the issue");
        }

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

        return ApiResponse.builder()
                .error(false)
                .message("Transfer done successfully")
                .data(transaction)
                .build();
    }

    public ApiResponse getTransaction(@Valid UUID id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser currentUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Invalid email or password"));

        Transaction transaction = transactionRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Invalid transaction"));

        boolean isSourceWallet = transaction.getSourceWallet() != null;
        boolean isReceiverWallet = transaction.getDestinationWallet() != null;

        boolean isSender = isSourceWallet &&
                currentUser.getId().equals(transaction.getSourceWallet().getUser().getId());

        boolean isReceiver = isReceiverWallet &&
                currentUser.getId().equals(transaction.getDestinationWallet().getUser().getId());

        if(!isSender && !isReceiver){
            throw new UnauthorizeUserException("You are not allowed to access this transaction");
        }

        TransactionResponseDto transactionRes = TransactionResponseDto.builder()
                .senderEmail(isSourceWallet ? transaction.getSourceWallet().getUser().getEmail() : "External Funding")
                .senderFullName(isSourceWallet ? transaction.getSourceWallet().getUser().getFullName() : "Paystack")
                .receiverEmail(transaction.getDestinationWallet().getUser().getEmail())
                .receiverFullName(transaction.getDestinationWallet().getUser().getFullName())
                .amount(transaction.getAmount())
                .externalReference(transaction.getExternalReference())
                .description(transaction.getDescription())
                .type(transaction.getType())
                .reference(transaction.getReference())
                .status(transaction.getStatus())
                .dateTime(transaction.getCreatedAt())
                .build();

        return ApiResponse.builder()
                .error(false)
                .message("Transaction")
                .data(transactionRes)
                .build();
    }

    public ApiResponse histories() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username and password"));

        List<Transaction> transactions = transactionRepo.findWalletHistory(user.getWallet().getId());

        if(transactions.isEmpty()){
            return ApiResponse.builder()
                    .error(false)
                    .message("This user has not perform any transaction")
                    .build();
        }

        var transactionsRes = transactions.stream().map(transaction -> {
            boolean isSourceWallet = transaction.getSourceWallet() != null;


            return TransactionResponseDto.builder()
                    .senderEmail(isSourceWallet ? transaction.getSourceWallet().getUser().getEmail() : "External Funding")
                    .senderFullName(isSourceWallet ? transaction.getSourceWallet().getUser().getFullName() : "Paystack")
                    .receiverEmail(transaction.getDestinationWallet().getUser().getEmail())
                    .receiverFullName(transaction.getDestinationWallet().getUser().getFullName())
                    .amount(transaction.getAmount())
                    .externalReference(transaction.getExternalReference())
                    .description(transaction.getDescription())
                    .type(transaction.getType())
                    .reference(transaction.getReference())
                    .status(transaction.getStatus())
                    .dateTime(transaction.getCreatedAt())
                    .build();
        }).toList();


        return ApiResponse.builder()
                .error(false)
                .message("This is the user transaction histories")
                .data(transactionsRes)
                .build();
    }

    @Transactional
    public ApiResponse withdraw(WithdrawalRequestDto input) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Invalid username or password"));

        Wallet wallet = walletRepo.findByIdWithLock(user.getWallet().getId())
                .orElseThrow(() -> new NoSuchElementException("Wallet does not exist"));

        if(!passwordEncoder.matches(input.pin(), wallet.getPin())){
            throw new BadCredentialsException("Incorrect pin");
        }

        boolean comparison = wallet.getBalance().compareTo(input.amount()) < 0;

        if(comparison){
            throw new InsufficientBalanceException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(input.amount()));
        String reference = UUID.randomUUID().toString();
        Transaction transaction = Transaction.builder()
                .amount(input.amount())
                .description("External mock withdrawal")
                .sourceWallet(wallet)
                .destinationWallet(null)
                .type(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.SUCCESS)
                .reference(reference)
                .build();

        walletRepo.save(wallet);
        transactionRepo.save(transaction);
        return ApiResponse.builder()
                .error(false)
                .message("Mock Withdrawal done successfully")
                .data(transaction)
                .build();
    }
}
