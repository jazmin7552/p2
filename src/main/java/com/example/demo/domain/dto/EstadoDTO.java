package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idEstado;

    @NotBlank(message = "El nombre es obligatorio", groups = { Create.class, Update.class })
    @Size(max = 10, message = "El nombre no puede exceder 10 caracteres")
    private String nombre;

    // Validation groups
    public interface Create {
    }

    public interface Update {
    }
}