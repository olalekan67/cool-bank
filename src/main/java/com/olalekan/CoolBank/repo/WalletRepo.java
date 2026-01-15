package com.olalekan.CoolBank.repo;

import com.olalekan.CoolBank.model.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id =:id")
    Optional<Wallet> findByIdWithLock(@Param("id") UUID id);

    Optional<Wallet> findByUserId(@Param("userId") UUID userId);
}
