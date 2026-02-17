package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.exception.ExpiredTokenException;
import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.RefreshToken;
import com.olalekan.CoolBank.repo.AppUserRepo;
import com.olalekan.CoolBank.repo.RefreshTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;
    private final AppUserRepo appUserRepo;

    @Transactional
    public RefreshToken createRefreshToken(AppUser user){
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        return refreshTokenRepo.save(refreshToken);
    }

    @Transactional
    public RefreshToken verifyRefreshToken(RefreshToken token){
        if(token.getExpiryDate().isBefore(LocalDateTime.now())){
            refreshTokenRepo.delete(token);
            throw new ExpiredTokenException("The token has expired");
        }
        return token;
    }

    public void deleteToken(String token){
        RefreshToken refreshToken = refreshTokenRepo.findByToken(token)
                .orElseThrow(() -> new NoSuchElementException("Invalid token"));

        refreshTokenRepo.delete(refreshToken);
    }

}
