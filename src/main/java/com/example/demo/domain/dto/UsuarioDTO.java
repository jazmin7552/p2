package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String idUsuario;

    @NotBlank(message = "El nombre es obligatorio", groups = {Create.class, Update.class})
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio", groups = {Create.class, Update.class})
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria", groups = {Create.class})
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres", groups = {Create.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "El rol es obligatorio", groups = {Create.class, Update.class})
    private Integer rolId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String rolNombre;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TelefonoDTO> telefonos;

    // Validation groups
    public interface Create {}
    public interface Update {}
}