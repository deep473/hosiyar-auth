package com.auth.assessment.controller;

import com.auth.assessment.dto.LoginRequestDTO;
import com.auth.assessment.dto.LoginResponseDTO;
import com.auth.assessment.dto.RegisterRequestDTO;
import com.auth.assessment.service.AppUserService;
import com.auth.assessment.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AppUserService appUserService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            AppUserService appUserService,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.appUserService = appUserService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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
    public ResponseEntity<LoginResponseDTO> login(
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

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password"
            );
        }
    }
}