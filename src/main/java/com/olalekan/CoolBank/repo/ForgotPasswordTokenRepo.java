package com.olalekan.CoolBank.repo;

import com.olalekan.CoolBank.model.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ForgotPasswordTokenRepo extends JpaRepository<ForgotPasswordToken, UUID> {

    Optional<ForgotPasswordToken> findByToken(String token);

    int deleteByExpiresAtBefore(LocalDateTime now);
}
