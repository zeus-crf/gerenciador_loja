package com.example.gerenciador_loja_backend.dtos;



public record ClienteDto(
        String name,

        String telefone,

        String email,

        String endereco,

        String notas) {

}
