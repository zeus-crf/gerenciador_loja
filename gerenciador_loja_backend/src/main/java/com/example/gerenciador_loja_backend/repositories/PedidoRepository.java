package com.example.gerenciador_loja_backend.repositories;

import com.example.gerenciador_loja_backend.enuns.StatusDePagamento;
import com.example.gerenciador_loja_backend.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    ResponseEntity<Object> findByStatusDePagamento(StatusDePagamento status);
}
