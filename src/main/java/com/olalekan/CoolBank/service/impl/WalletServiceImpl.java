package com.olalekan.CoolBank.service.impl;

import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.Wallet;
import com.olalekan.CoolBank.model.dto.request.CreatePinRequestDto;
import com.olalekan.CoolBank.model.dto.request.UpdatePinRequestDto;
import com.olalekan.CoolBank.model.dto.response.BalanceResponseDto;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.repo.AppUserRepo;
import com.olalekan.CoolBank.repo.WalletRepo;
import com.olalekan.CoolBank.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepo walletRepo;
    private final AppUserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public BaseResponseDto createPin(CreatePinRequestDto requestDto) {
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

    public BalanceResponseDto getBalance() {

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

    @Transactional
    public BaseResponseDto changePin(UpdatePinRequestDto requestDto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or Password"));

        if(!passwordEncoder.matches(requestDto.password(), user.getPassword())){
           throw new BadCredentialsException("Incorrect password");
        }

        Wallet wallet = user.getWallet();

        if(!passwordEncoder.matches(requestDto.oldPin(), wallet.getPin())){
            throw new BadCredentialsException("Old pin is not correct");
        }

        wallet.setPin(passwordEncoder.encode(requestDto.newPin()));
        walletRepo.save(wallet);
        return BaseResponseDto.builder()
                .message("Pin updated successfully")
                .build();
    }
}
