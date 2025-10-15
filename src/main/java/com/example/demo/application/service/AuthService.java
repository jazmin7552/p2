package com.example.demo.application.service;

import com.example.demo.domain.entity.Rol;
import com.example.demo.domain.entity.Usuario;
import com.example.demo.infrastructure.web.dto.LoginDTO;
import com.example.demo.infrastructure.web.dto.RegisterDTO;
import com.example.demo.infrastructure.web.dto.JwtResponseDTO;
import com.example.demo.infrastructure.persistence.repository.RolRepository;
import com.example.demo.infrastructure.persistence.repository.UsuarioRepository;
import com.example.demo.infrastructure.config.JwtTokenProvider;
import com.example.demo.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtResponseDTO login(LoginDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));

        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        String token = jwtTokenProvider.generateToken(authentication);

        return JwtResponseDTO.builder()
                .token(token)
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .build();
    }

    public JwtResponseDTO register(RegisterDTO registerRequest) {
        if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        Rol rol = rolRepository.findById(1) // Rol por defecto: Usuario
                .orElseThrow(() -> new BadRequestException("Rol no encontrado"));

        Usuario usuario = Usuario.builder()
                .nombre(registerRequest.getNombre())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .rol(rol)
                .build();

        usuarioRepository.save(usuario);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getPassword()));

        String token = jwtTokenProvider.generateToken(authentication);

        return JwtResponseDTO.builder()
                .token(token)
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .build();
    }
}