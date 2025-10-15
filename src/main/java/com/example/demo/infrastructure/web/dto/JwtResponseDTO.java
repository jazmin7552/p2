package com.example.demo.infrastructure.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDTO {

    private String token;

    @Builder.Default
    private String type = "Bearer";

    private String email;
    private String nombre;
}