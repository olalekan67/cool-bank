package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.Utils.ActiveStatus;
import com.olalekan.CoolBank.Utils.KycTier;
import com.olalekan.CoolBank.Utils.TokenUtils;
import com.olalekan.CoolBank.Utils.UserRole;
import com.olalekan.CoolBank.event.RegistrationCompleteEvent;
import com.olalekan.CoolBank.exception.DuplicateResourceException;
import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.Token;
import com.olalekan.CoolBank.model.Wallet;
import com.olalekan.CoolBank.model.dto.RegisterRequestDto;
import com.olalekan.CoolBank.model.dto.RegisterResponseDto;
import com.olalekan.CoolBank.repo.AppUserRepo;
import com.olalekan.CoolBank.repo.RefreshTokenRepo;
import com.olalekan.CoolBank.repo.TokenRepo;
import com.olalekan.CoolBank.repo.WalletRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AppUserRepo userRepo;
    private final TokenRepo tokenRepo;
    private final WalletRepo walletRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final EmailSenderService emailSenderService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public RegisterResponseDto register(@Valid RegisterRequestDto registerRequestDto) {



        if(userRepo.existsByEmail(registerRequestDto.email())){
            throw new DuplicateResourceException("Email already exist");
        }

        if(userRepo.existsByPhoneNumber(registerRequestDto.phoneNumber())){
            throw new DuplicateResourceException("Phone number already exists");
        }

        AppUser user = AppUser.builder()
                .activeStatus(ActiveStatus.PENDING_VERIFICATION)
                .email(registerRequestDto.email())
                .fullName(registerRequestDto.fullName())
                .role(UserRole.USER)
                .kycTier(KycTier.TIER_1)
                .password(passwordEncoder.encode(registerRequestDto.password()))
                .phoneNumber(registerRequestDto.phoneNumber())
                .build();

        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .currency("NGN")
                .build();

        user.setWallet(wallet);
        userRepo.save(user);
        walletRepo.save(wallet);

        String generatedToken = TokenUtils.generateToken();

        Token token = Token.builder()
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .token(generatedToken)
                .build();


        tokenRepo.save(token);


        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, generatedToken));

        return RegisterResponseDto.builder()
                .message("User registered successfully, please go ahead and verify your email.")
                .build();
    }
}
