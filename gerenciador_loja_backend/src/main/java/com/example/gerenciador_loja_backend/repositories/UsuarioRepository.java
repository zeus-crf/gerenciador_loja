package com.example.gerenciador_loja_backend.repositories;

import com.example.gerenciador_loja_backend.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findAll(String usermane);
}
