package com.olalekan.CoolBank.repo;

import com.olalekan.CoolBank.model.AdminActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminActionLogRepo extends JpaRepository<AdminActionLog, UUID> {
}
