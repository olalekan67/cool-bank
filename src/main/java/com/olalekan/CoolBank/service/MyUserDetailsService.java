package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.MyPrincipal;
import com.olalekan.CoolBank.repo.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final AppUserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid user"));
        return new MyPrincipal(user);
    }
}
