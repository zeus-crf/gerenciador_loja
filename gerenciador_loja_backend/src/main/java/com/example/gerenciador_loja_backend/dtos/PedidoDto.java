package com.example.gerenciador_loja_backend.dtos;

import com.example.gerenciador_loja_backend.enuns.StatusDePagamento;

import java.util.List;
import java.util.UUID;

public record PedidoDto (UUID idCliente,
                         List<String> itensList,
                         StatusDePagamento statusDePagamento,
                         Integer parcelasRestantes){
}
