package com.olalekan.CoolBank.model;

import com.olalekan.CoolBank.Utils.AdminActionType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin_audit_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminActionLog extends BaseEntity {

    @ManyToOne
    private AppUser admin;

    private AdminActionType action;

    private String targetId;

    private String reason;

}
