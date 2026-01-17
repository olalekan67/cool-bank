package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.Wallet;
import com.olalekan.CoolBank.model.dto.BalanceResponseDto;
import com.olalekan.CoolBank.model.dto.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.CreatePinRequestDto;
import com.olalekan.CoolBank.repo.AppUserRepo;
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

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepo walletRepo;
    private final AppUserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public BaseResponseDto createPin(@Valid CreatePinRequestDto requestDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(email == null){
            throw new UsernameNotFoundException("Please login your account to access this route");
        }

        AppUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Invalid username or password"));

        if(!passwordEncoder.matches(requestDto.password(), user.getPassword())){
            throw new BadCredentialsException("Invalid User");
        }

        Wallet wallet = walletRepo.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("Invalid User"));

        if(wallet.getPin() != null){
            throw new IllegalStateException("Pin already set.");
        }

        wallet.setPin(passwordEncoder.encode(requestDto.pin()));
        walletRepo.save(wallet);

        return BaseResponseDto.builder()
                .message("Pin set successfully")
                .build();

    }

    public BalanceResponseDto balance() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Invalid username or password"));

        Wallet wallet = walletRepo.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("Invalid user"));

        return BalanceResponseDto.builder()
                .id(wallet.getId())
                .fullName(user.getFullName())
                .balance(wallet.getBalance())
                .build();
    }
}
