package com.olalekan.CoolBank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Wallet extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "user_id",nullable = false )
    private AppUser user;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private String currency = "NGN";

    private String pin;

    @Version
    private Long version;

}
