package com.auth.assessment.service;

import com.auth.assessment.dto.RegisterRequestDTO;
import com.auth.assessment.entity.AppUser;
import com.auth.assessment.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    final AppUserRepository appUserRepository;
    final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(RegisterRequestDTO registerRequestDTO) {

        if (appUserRepository.existsByUsername(registerRequestDTO.username()))
            return "Username already exists!";
        if (appUserRepository.existsByEmail(registerRequestDTO.email()))
            return "Email already exists!";

        AppUser appUser = new AppUser();
        appUser.setUsername(registerRequestDTO.username());
        appUser.setEmail(registerRequestDTO.email());
        appUser.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
        appUser.setRole(registerRequestDTO.role());

        appUserRepository.save(appUser);
        return "User registered successfully!";
    }
}
