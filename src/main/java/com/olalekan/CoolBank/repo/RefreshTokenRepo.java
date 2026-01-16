package com.olalekan.CoolBank.repo;

import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    void deleteByUser(AppUser user);
}
