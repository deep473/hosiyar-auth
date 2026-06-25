package com.auth.assessment.controller;

import com.auth.assessment.dto.LoginRequestDTO;
import com.auth.assessment.dto.LoginResponseDTO;
import com.auth.assessment.dto.RegisterRequestDTO;
import com.auth.assessment.service.AppUserService;
import com.auth.assessment.service.JwtService;
import com.auth.assessment.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    final AppUserService appUserService;
    final AuthenticationManager authenticationManager;
    final JwtService jwtService;
    final TokenBlacklistService tokenBlacklistService;

    public AuthController(
            AppUserService appUserService,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            TokenBlacklistService tokenBlacklistService
    ) {
        this.appUserService = appUserService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        String response = appUserService.register(registerRequestDTO);

        if (response.equals("User registered successfully!")) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.username(),
                            loginRequestDTO.password()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new LoginResponseDTO(token));

        } catch (AuthenticationException exception) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Token is missing");
        }

        String token = authHeader.substring(7);

        tokenBlacklistService.blacklistToken(token);

        return ResponseEntity.ok("Logged out successfully");
    }
}