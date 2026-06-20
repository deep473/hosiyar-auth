package com.auth.assessment.controller;

import com.auth.assessment.dto.RegisterRequestDTO;
import com.auth.assessment.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    final AppUserService appUserService;

    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO){
        String response = appUserService.register(registerRequestDTO);

        if(response.equals("User registered successfully!"))
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
        else
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(response);

    }


}
