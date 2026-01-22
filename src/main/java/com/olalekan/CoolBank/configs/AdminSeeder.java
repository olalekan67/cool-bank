package com.olalekan.CoolBank.configs;

import com.olalekan.CoolBank.Utils.ActiveStatus;
import com.olalekan.CoolBank.Utils.KycTier;
import com.olalekan.CoolBank.Utils.UserRole;
import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.Wallet;
import com.olalekan.CoolBank.repo.AppUserRepo;
import com.olalekan.CoolBank.repo.WalletRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {
    private final AppUserRepo userRepo;
    private final WalletRepo walletRepo;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {

        String adminEmail = "admin@coolBank.com";

        if(userRepo.findByEmail(adminEmail).isEmpty()){

            AppUser adminUser = AppUser.builder()
                    .role(UserRole.ADMIN)
                    .phoneNumber("0000000000")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("SecretAdmin123"))
                    .fullName("Abdulsalam Olawale")
                    .kycTier(KycTier.TIER_3)
                    .activeStatus(ActiveStatus.ACTIVE)
                    .build();

            Wallet adminWallet = Wallet.builder()
                    .balance(BigDecimal.ZERO)
                    .pin(passwordEncoder.encode("0000"))
                    .user(adminUser)
                    .currency("NGN")
                    .build();

            adminUser.setWallet(adminWallet);
            userRepo.save(adminUser);
            walletRepo.save(adminWallet);

            System.out.println("=================================");
            System.out.println("ADMIN SEEDED SUCCESSFULLY");
            System.out.println("Email: " + adminEmail);
            System.out.println("Password: "+ "SecretAdmin123");
            System.out.println("=================================");
        }

    }
}
