package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.Utils.ActiveStatus;
import com.olalekan.CoolBank.Utils.KycTier;
import com.olalekan.CoolBank.Utils.TokenUtils;
import com.olalekan.CoolBank.Utils.UserRole;
import com.olalekan.CoolBank.event.RegistrationCompleteEvent;
import com.olalekan.CoolBank.exception.DuplicateResourceException;
import com.olalekan.CoolBank.exception.ExpiredTokenException;
import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.RefreshToken;
import com.olalekan.CoolBank.model.Token;
import com.olalekan.CoolBank.model.Wallet;
import com.olalekan.CoolBank.model.dto.*;
import com.olalekan.CoolBank.repo.AppUserRepo;
import com.olalekan.CoolBank.repo.RefreshTokenRepo;
import com.olalekan.CoolBank.repo.TokenRepo;
import com.olalekan.CoolBank.repo.WalletRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AppUserRepo userRepo;
    private final TokenRepo tokenRepo;
    private final WalletRepo walletRepo;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepo refreshTokenRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public BaseResponseDto register(@Valid RegisterRequestDto registerRequestDto) {

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

        return BaseResponseDto.builder()
                .message("User registered successfully, please go ahead and verify your email.")
                .build();
    }


    @Transactional
    public BaseResponseDto verify(String userToken) {
        Token token = tokenRepo.findByToken(userToken)
                .orElseThrow(() -> new NoSuchElementException("Invalid token"));

        if(token.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new ExpiredTokenException("This token is expired");
        }

        AppUser user = token.getUser();

        if(user.getActiveStatus() == ActiveStatus.ACTIVE){
            return BaseResponseDto.builder()
                    .message("User has already been verified")
                    .build();
        }

        user.setActiveStatus(ActiveStatus.ACTIVE);
        userRepo.save(user);

        tokenRepo.delete(token);

        return BaseResponseDto.builder()
                .message("User verified successfully")
                .build();
    }

    @Transactional
    public LoginResponseDto login(@Valid LoginRequestDto requestDto) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.email(),
                        requestDto.password()
                )
        );


        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        AppUser user = userRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid user"));

        if(refreshTokenRepo.existsByUser(user)){
            refreshTokenRepo.deleteByUser(user);
        }

        String accessToken = jwtService.generateToken(userDetails.getUsername());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .message("User logged in successfully")
                .build();
    }

    public LoginResponseDto refreshToken(@Valid RefreshTokenRequestDto requestDto) {

        RefreshToken refreshToken = refreshTokenRepo.findByToken(requestDto.refreshToken())
                .orElseThrow(()-> new NoSuchElementException("Invalid refresh token"));

        refreshTokenService.verifyRefreshToken(refreshToken);

        AppUser user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user.getEmail());

        return LoginResponseDto.builder()
                .refreshToken(refreshToken.getToken())
                .accessToken(accessToken)
                .message("Token refreshed successfully.")
                .build();
    }
}
