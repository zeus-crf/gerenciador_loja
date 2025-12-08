package com.example.gerenciador_loja_backend.controllers;

import com.example.gerenciador_loja_backend.dtos.ClienteDto;
import com.example.gerenciador_loja_backend.models.Cliente;
import com.example.gerenciador_loja_backend.repositories.ClienteRepository;
import com.example.gerenciador_loja_backend.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/clientes")
    public ResponseEntity<Cliente> addCliente(@RequestBody @Valid ClienteDto clienteDto) {
        Cliente cliente = clienteService.criarCliente(clienteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @GetMapping("/clientes")
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> getAllClientes() {
        List<Cliente> clientes = clienteService.listarClientes();

        List<EntityModel<Cliente>> clientesModel = clientes.stream()
                .map(cliente -> EntityModel.of(cliente,
                        linkTo(methodOn(ClienteController.class).getOneCliente(cliente.getId())).withSelfRel()
                ))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(clientesModel));
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<Cliente> getOneCliente(@PathVariable UUID id) {
        return clienteService.buscarClientePorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<Cliente> editCliente(@PathVariable UUID id, @RequestBody ClienteDto clienteDto) {
        return clienteService.atualizarCliente(id, clienteDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<String> deleteCliente(@PathVariable UUID id) {
        boolean deletado = clienteService.deletarCliente(id);
        if (deletado) {
            return ResponseEntity.ok("Cliente excluído com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }
    }
}
