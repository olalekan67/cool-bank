package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.model.RefreshToken;
import com.olalekan.CoolBank.repo.AppUserRepo;
import com.olalekan.CoolBank.repo.RefreshTokenRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;
    private final AppUserRepo appUserRepo;

    @Transactional
    public RefreshToken createRefreshToken(String email){
        RefreshToken refreshToken = RefreshToken.builder()
                .user(appUserRepo.findByEmail(email).orElseThrow(() ->
                        new UsernameNotFoundException("Invalid user")))
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        return refreshTokenRepo.save(refreshToken);
    }

    @Transactional
    public RefreshToken verifyRefreshToken(RefreshToken token){
        if(token.getExpiryDate().isBefore(LocalDateTime.now())){
            refreshTokenRepo.delete(token);
            throw new RuntimeException("The token has expired");
        }
        return token;
    }

    public void deleteToken(String token){
        RefreshToken refreshToken = refreshTokenRepo.findByToken(token)
                .orElseThrow(() -> new NoSuchElementException("Invalid token"));

        refreshTokenRepo.delete(refreshToken);
    }

}
