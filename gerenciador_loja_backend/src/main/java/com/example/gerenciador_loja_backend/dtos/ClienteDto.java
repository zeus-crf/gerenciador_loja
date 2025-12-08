package com.example.gerenciador_loja_backend.dtos;

import jakarta.validation.constraints.NotBlank;

public record ClienteDto(
        @NotBlank
        String name,

        @NotBlank
        String telefone,

        @NotBlank
        String email,

        @NotBlank
        String endereco,

        String notas) {
}
