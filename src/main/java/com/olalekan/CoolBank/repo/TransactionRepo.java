package com.olalekan.CoolBank.repo;

import com.olalekan.CoolBank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, UUID> {

    boolean existsByReference(String reference);

    Optional<Transaction> findByExternalReference(String reference);

    Optional<Transaction> findByReference(String reference);

    @Query("SELECT t FROM Transaction t " +
                    "LEFT JOIN FETCH t.sourceWallet s " +
                    "LEFT JOIN FETCH s.user " +
                    "LEFT JOIN FETCH t.destinationWallet d " +
                    "LEFT JOIN FETCH d.user " +
                    "WHERE s.id = :walletId OR d.id = :walletId " +
                    "ORDER BY t.createdAt DESC")
    List<Transaction> findWalletHistory(@Param("walletId") UUID walletId);
}
