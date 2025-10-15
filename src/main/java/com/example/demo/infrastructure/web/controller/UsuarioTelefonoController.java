package com.example.demo.infrastructure.web.controller;

import com.example.demo.application.service.UsuarioService;
import com.example.demo.domain.dto.TelefonoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/{idUsuario}/telefonos")
@RequiredArgsConstructor
@Tag(name = "Usuario-Teléfonos", description = "Gestión de teléfonos de usuarios")
public class UsuarioTelefonoController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener teléfonos de un usuario")
    public ResponseEntity<List<TelefonoDTO>> getTelefonosByUsuario(@PathVariable String idUsuario) {
        List<TelefonoDTO> telefonos = usuarioService.obtenerTelefonos(idUsuario);
        return ResponseEntity.ok(telefonos);
    }

    @PostMapping("/{idTelefono}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    @Operation(summary = "Agregar teléfono a usuario")
    public ResponseEntity<Void> agregarTelefono(
            @PathVariable String idUsuario,
            @PathVariable Integer idTelefono) {
        usuarioService.agregarTelefono(idUsuario, idTelefono);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idTelefono}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    @Operation(summary = "Remover teléfono de usuario")
    public ResponseEntity<Void> removerTelefono(
            @PathVariable String idUsuario,
            @PathVariable Integer idTelefono) {
        usuarioService.removerTelefono(idUsuario, idTelefono);
        return ResponseEntity.noContent().build();
    }
}
