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
public class TelefonoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idTelefono;

    @NotBlank(message = "El número es obligatorio", groups = { Create.class, Update.class })
    @Size(max = 15, message = "El número no puede exceder 15 caracteres")
    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "Formato de número inválido")
    private String numero;

    // Validation groups
    public interface Create {
    }

    public interface Update {
    }
}