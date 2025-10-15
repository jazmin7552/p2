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
public class MesaDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idMesa;

    @NotNull(message = "La capacidad es obligatoria", groups = {Create.class, Update.class})
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacidad;

    @NotBlank(message = "La ubicación es obligatoria", groups = {Create.class, Update.class})
    @Size(max = 50, message = "La ubicación no puede exceder 50 caracteres")
    private String ubicacion;

    @NotNull(message = "El estado es obligatorio", groups = {Create.class, Update.class})
    private Integer estadoId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String estadoNombre;

    // Validation groups
    public interface Create {}
    public interface Update {}
}