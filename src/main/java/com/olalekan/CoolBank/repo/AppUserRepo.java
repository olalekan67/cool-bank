package com.olalekan.CoolBank.repo;

import com.olalekan.CoolBank.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByEmail(@Param("email") String email);
    boolean existsByEmail(@Param("email") String email);
    boolean existsByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
