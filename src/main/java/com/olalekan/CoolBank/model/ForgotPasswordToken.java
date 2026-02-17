package com.olalekan.CoolBank.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ForgotPasswordToken extends BaseEntity{

    @Column(unique = true)
    private String token;
    private LocalDateTime expiresAt;
    @OneToOne
    private AppUser user;
}
