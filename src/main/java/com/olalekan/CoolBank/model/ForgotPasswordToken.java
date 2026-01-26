package com.olalekan.CoolBank.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
public class ForgotPasswordToken extends BaseEntity{

    @Column(unique = true)
    private String token;
    private LocalDateTime expiresAt;
    @OneToOne
    private AppUser user;
}
