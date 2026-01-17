package com.olalekan.CoolBank.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Getter
@Setter
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
