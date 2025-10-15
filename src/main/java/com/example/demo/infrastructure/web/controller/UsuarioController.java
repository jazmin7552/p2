package com.example.demo.infrastructure.web.controller;

import com.example.demo.domain.dto.UsuarioDTO;
import com.example.demo.application.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna la lista completa de usuarios. Solo accesible por ADMIN.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.hasUserId(authentication, #id)")
    @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario específico. ADMIN puede ver cualquier usuario, usuarios normales solo pueden ver su propio perfil.")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable String id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario en el sistema. Solo accesible por ADMIN.")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody UsuarioDTO request) {
        UsuarioDTO nuevoUsuario = usuarioService.crearUsuario(request);
        return ResponseEntity.status(201).body(nuevoUsuario);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.hasUserId(authentication, #id)")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente. ADMIN puede actualizar cualquier usuario, usuarios normales solo pueden actualizar su propio perfil.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
    public ResponseEntity<UsuarioDTO> updateUsuario(
            @PathVariable String id,
            @Valid @RequestBody UsuarioDTO request) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, request);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema. Solo accesible por ADMIN.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente")
    public ResponseEntity<Void> deleteUsuario(@PathVariable String id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // Request param para evitar problemas con caracteres especiales en el email
    @GetMapping("/by-email")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener usuario por email", description = "Busca un usuario por su dirección de correo electrónico. Solo accesible por ADMIN.")
    public ResponseEntity<UsuarioDTO> getUsuarioByEmail(@RequestParam("email") String email) {
        return usuarioService.obtenerUsuarioPorCorreo(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rol/{rol}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener usuarios por rol", description = "Retorna todos los usuarios que tienen un rol específico. Solo accesible por ADMIN.")
    public ResponseEntity<List<UsuarioDTO>> getUsuariosByRol(@PathVariable String rol) {
        List<UsuarioDTO> usuarios = usuarioService.obtenerUsuariosPorRol(rol);
        return ResponseEntity.ok(usuarios);
    }
}
