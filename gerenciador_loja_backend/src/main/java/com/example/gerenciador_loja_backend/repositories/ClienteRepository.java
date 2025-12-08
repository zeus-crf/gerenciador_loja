package com.example.gerenciador_loja_backend.repositories;

import com.example.gerenciador_loja_backend.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
}
