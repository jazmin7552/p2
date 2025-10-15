package com.example.demo.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoriaDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idCategoria;

    @NotBlank(message = "El nombre es obligatorio", groups = { Create.class, Update.class })
    @Size(max = 20, message = "El nombre no puede exceder 20 caracteres")
    private String nombre;

    // Validation groups
    public interface Create {
    }

    public interface Update {
    }
}