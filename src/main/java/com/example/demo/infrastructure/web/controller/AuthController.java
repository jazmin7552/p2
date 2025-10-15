package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.AuthService;
import com.example.demo.infrastructure.web.dto.JwtResponseDTO;
import com.example.demo.infrastructure.web.dto.LoginDTO;
import com.example.demo.infrastructure.web.dto.RegisterDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para login y registro de usuarios")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve un token JWT")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginDTO loginRequest) {
        // TODO: Implementar lógica de login
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario en el sistema")
    public ResponseEntity<JwtResponseDTO> register(@Valid @RequestBody RegisterDTO registerRequest) {
        // TODO: Implementar lógica de registro
        return ResponseEntity.ok().build();
    }
}
