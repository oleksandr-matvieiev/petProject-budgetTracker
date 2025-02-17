package com.example.petprojectbudgettracker.security;

import com.example.petprojectbudgettracker.models.User;
import com.example.petprojectbudgettracker.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email));
    return org.springframework.security.core.userdetails.User
            .withUsername(email)
            .password(user.getPassword())
            .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name()))).build();
    }
}
