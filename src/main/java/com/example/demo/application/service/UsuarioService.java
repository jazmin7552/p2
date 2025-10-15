package com.example.demo.application.service;

import com.example.demo.domain.dto.TelefonoDTO;
import com.example.demo.domain.dto.UsuarioDTO;
import com.example.demo.domain.entity.Rol;
import com.example.demo.domain.entity.Telefono;
import com.example.demo.domain.entity.Usuario;
import com.example.demo.infrastructure.persistence.repository.RolRepository;
import com.example.demo.infrastructure.persistence.repository.TelefonoRepository;
import com.example.demo.infrastructure.persistence.repository.UsuarioRepository;
import com.example.demo.shared.exception.BadRequestException;
import com.example.demo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final TelefonoRepository telefonoRepository;
    private final PasswordEncoder passwordEncoder;

    // ------------------------
    // Métodos usados por Controller (DTOs)
    // ------------------------

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> obtenerUsuarioPorId(String idUsuario) {
        return usuarioRepository.findById(idUsuario).map(this::toDto);
    }

    public UsuarioDTO crearUsuario(UsuarioDTO request) {
        if (request == null)
            throw new BadRequestException("Datos de usuario inválidos");
        if (request.getEmail() == null || request.getEmail().trim().isEmpty())
            throw new BadRequestException("El email es obligatorio");

        if (usuarioRepository.existsByEmail(request.getEmail()))
            throw new BadRequestException("El email ya está registrado");

        Usuario usuario = toEntity(request);

        // rol
        if (request.getRolId() != null) {
            Rol rol = rolRepository.findById(request.getRolId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Rol no encontrado con ID: " + request.getRolId()));
            usuario.setRol(rol);
        }

        // contraseña obligatoria al crear
        if (request.getPassword() == null || request.getPassword().isEmpty())
            throw new BadRequestException("La contraseña es obligatoria");
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        // manejar telefonos si vienen en el DTO (crear o asociar)
        if (request.getTelefonos() != null && !request.getTelefonos().isEmpty()) {
            List<Telefono> telefonos = new ArrayList<>();
            for (TelefonoDTO tDto : request.getTelefonos()) {
                if (tDto.getIdTelefono() != null) {
                    Telefono t = telefonoRepository.findById(tDto.getIdTelefono())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Teléfono no encontrado con ID: " + tDto.getIdTelefono()));
                    telefonos.add(t);
                } else {
                    Telefono nuevo = new Telefono();
                    nuevo.setNumero(tDto.getNumero());
                    telefonos.add(telefonoRepository.save(nuevo));
                }
            }
            usuario.setTelefonos(telefonos);
        } else {
            usuario.setTelefonos(new ArrayList<>());
        }

        Usuario saved = usuarioRepository.save(usuario);
        return toDto(saved);
    }

    public UsuarioDTO actualizarUsuario(String idUsuario, UsuarioDTO request) {
        if (request == null)
            throw new BadRequestException("Datos de usuario inválidos");

        Usuario existente = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(existente.getEmail())) {
            if (usuarioRepository.existsByEmail(request.getEmail()))
                throw new BadRequestException("El email ya está registrado");
            existente.setEmail(request.getEmail());
        }

        if (request.getNombre() != null)
            existente.setNombre(request.getNombre());

        if (request.getRolId() != null) {
            Rol rol = rolRepository.findById(request.getRolId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Rol no encontrado con ID: " + request.getRolId()));
            existente.setRol(rol);
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            existente.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getTelefonos() != null) {
            List<Telefono> telefonos = new ArrayList<>();
            for (TelefonoDTO tDto : request.getTelefonos()) {
                if (tDto.getIdTelefono() != null) {
                    Telefono t = telefonoRepository.findById(tDto.getIdTelefono())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Teléfono no encontrado con ID: " + tDto.getIdTelefono()));
                    if (tDto.getNumero() != null) {
                        t.setNumero(tDto.getNumero());
                        telefonoRepository.save(t);
                    }
                    telefonos.add(t);
                } else {
                    Telefono nuevo = new Telefono();
                    nuevo.setNumero(tDto.getNumero());
                    telefonos.add(telefonoRepository.save(nuevo));
                }
            }
            existente.setTelefonos(telefonos);
        }

        Usuario updated = usuarioRepository.save(existente);
        return toDto(updated);
    }

    public void eliminarUsuario(String idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario);
        }
        usuarioRepository.deleteById(idUsuario);
    }

    public Optional<UsuarioDTO> obtenerUsuarioPorCorreo(String email) {
        return usuarioRepository.findByEmail(email).map(this::toDto);
    }

    public List<UsuarioDTO> obtenerUsuariosPorRol(String rolNombreOrId) {
        List<Usuario> usuarios;
        try {
            Integer idRol = Integer.valueOf(rolNombreOrId);
            usuarios = usuarioRepository.findByRol_IdRol(idRol);
        } catch (NumberFormatException e) {
            usuarios = usuarioRepository.findByRol_NombreIgnoreCase(rolNombreOrId);
        }
        return usuarios.stream().map(this::toDto).collect(Collectors.toList());
    }

    // ====================================
    // MÉTODOS DE GESTIÓN DE TELÉFONOS (compatibles con tus controladores)
    // ====================================

    public List<TelefonoDTO> obtenerTelefonos(String idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));
        if (usuario.getTelefonos() == null)
            return Collections.emptyList();
        return usuario.getTelefonos().stream()
                .map(this::toTelefonoDto)
                .collect(Collectors.toList());
    }

    public void agregarTelefono(String idUsuario, Integer idTelefono) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));
        Telefono telefono = telefonoRepository.findById(idTelefono)
                .orElseThrow(() -> new ResourceNotFoundException("Teléfono no encontrado con ID: " + idTelefono));

        if (usuario.getTelefonos() == null)
            usuario.setTelefonos(new ArrayList<>());
        if (!usuario.getTelefonos().stream().anyMatch(t -> t.getIdTelefono().equals(idTelefono))) {
            usuario.getTelefonos().add(telefono);
            usuarioRepository.save(usuario);
        }
    }

    public TelefonoDTO agregarTelefonoNuevo(String idUsuario, TelefonoDTO telefonoDTO) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        Telefono telefono = new Telefono();
        telefono.setNumero(telefonoDTO.getNumero());
        Telefono saved = telefonoRepository.save(telefono);

        if (usuario.getTelefonos() == null)
            usuario.setTelefonos(new ArrayList<>());
        usuario.getTelefonos().add(saved);
        usuarioRepository.save(usuario);

        return toTelefonoDto(saved);
    }

    public void removerTelefono(String idUsuario, Integer idTelefono) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));
        if (usuario.getTelefonos() == null)
            return;
        boolean removed = usuario.getTelefonos().removeIf(t -> t.getIdTelefono().equals(idTelefono));
        if (removed)
            usuarioRepository.save(usuario);
    }

    // ------------------------
    // Helpers de conversión
    // ------------------------

    private UsuarioDTO toDto(Usuario usuario) {
        if (usuario == null)
            return null;
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(readUsuarioId(usuario));
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setPassword(null); // no devolver password
        if (usuario.getRol() != null) {
            dto.setRolId(usuario.getRol().getIdRol());
            dto.setRolNombre(usuario.getRol().getNombre());
        }
        if (usuario.getTelefonos() != null) {
            dto.setTelefonos(usuario.getTelefonos().stream()
                    .map(this::toTelefonoDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private Usuario toEntity(UsuarioDTO dto) {
        if (dto == null)
            return null;
        Usuario u = new Usuario();
        writeUsuarioId(u, dto.getIdUsuario());
        u.setNombre(dto.getNombre());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword()); // contraseña en claro; será encriptada por el servicio
        if (dto.getRolId() != null) {
            Rol r = new Rol();
            r.setIdRol(dto.getRolId());
            u.setRol(r);
        }
        if (dto.getTelefonos() != null) {
            List<Telefono> telefonos = dto.getTelefonos().stream()
                    .map(tDto -> {
                        if (tDto.getIdTelefono() != null) {
                            Telefono t = new Telefono();
                            t.setIdTelefono(tDto.getIdTelefono());
                            t.setNumero(tDto.getNumero());
                            return t;
                        } else {
                            Telefono t = new Telefono();
                            t.setNumero(tDto.getNumero());
                            return t;
                        }
                    })
                    .collect(Collectors.toList());
            u.setTelefonos(telefonos);
        } else {
            u.setTelefonos(new ArrayList<>());
        }
        return u;
    }

    private TelefonoDTO toTelefonoDto(Telefono t) {
        if (t == null)
            return null;
        TelefonoDTO dto = new TelefonoDTO();
        dto.setIdTelefono(t.getIdTelefono());
        dto.setNumero(t.getNumero());
        return dto;
    }

    // =====================================================
    // Reflection helpers para leer/escribir id (id o idUsuario)
    // =====================================================
    private String readUsuarioId(Usuario usuario) {
        if (usuario == null)
            return null;
        try {
            // try getId()
            try {
                Method m = Usuario.class.getMethod("getId");
                Object val = m.invoke(usuario);
                return val != null ? val.toString() : null;
            } catch (NoSuchMethodException ignored) {
            }
            // try getIdUsuario()
            try {
                Method m2 = Usuario.class.getMethod("getIdUsuario");
                Object val = m2.invoke(usuario);
                return val != null ? val.toString() : null;
            } catch (NoSuchMethodException ignored) {
            }
            // try field "id"
            try {
                Field f = Usuario.class.getDeclaredField("id");
                f.setAccessible(true);
                Object val = f.get(usuario);
                return val != null ? val.toString() : null;
            } catch (NoSuchFieldException ignored) {
            }
            // try field "idUsuario"
            try {
                Field f2 = Usuario.class.getDeclaredField("idUsuario");
                f2.setAccessible(true);
                Object val = f2.get(usuario);
                return val != null ? val.toString() : null;
            } catch (NoSuchFieldException ignored) {
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private void writeUsuarioId(Usuario usuario, String idValue) {
        if (usuario == null)
            return;
        try {
            try {
                Method m = Usuario.class.getMethod("setId", String.class);
                m.invoke(usuario, idValue);
                return;
            } catch (NoSuchMethodException ignored) {
            }
            try {
                Method m2 = Usuario.class.getMethod("setIdUsuario", String.class);
                m2.invoke(usuario, idValue);
                return;
            } catch (NoSuchMethodException ignored) {
            }
            try {
                Field f = Usuario.class.getDeclaredField("id");
                f.setAccessible(true);
                f.set(usuario, idValue);
                return;
            } catch (NoSuchFieldException ignored) {
            }
            try {
                Field f2 = Usuario.class.getDeclaredField("idUsuario");
                f2.setAccessible(true);
                f2.set(usuario, idValue);
                return;
            } catch (NoSuchFieldException ignored) {
            }
        } catch (Exception ignored) {
        }
    }
}
