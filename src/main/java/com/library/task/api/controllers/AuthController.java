package com.library.task.api.controllers;

import com.library.task.service.autentication.IAuthenticationService;
import com.library.task.shared.dtos.auth.AuthResponseDto;
import com.library.task.shared.dtos.auth.LogInRequestDto;
import com.library.task.shared.dtos.auth.SignUpRequestDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthenticationService authenticationService;

    @Autowired
    public AuthController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> createAuthenticationToken(@Valid @RequestBody LogInRequestDto authenticationRequest) throws Exception {
        AuthResponseDto logInRespons= authenticationService.loginService(authenticationRequest);
        return ResponseEntity.ok(logInRespons);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequestDto signUpRequest) throws Exception {
        System.out.println(signUpRequest.getEmail());
        return ResponseEntity.ok(authenticationService.signUpService(signUpRequest));
    }
}