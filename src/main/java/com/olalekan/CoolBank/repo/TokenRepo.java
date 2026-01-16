package com.olalekan.CoolBank.repo;

import com.olalekan.CoolBank.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TokenRepo extends JpaRepository<Token, UUID> {

    int deleteByExpiredAtBefore(LocalDateTime time);

}
