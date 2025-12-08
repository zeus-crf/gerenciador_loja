package com.example.gerenciador_loja_backend.controllers;

import com.example.gerenciador_loja_backend.dtos.PedidoDto;
import com.example.gerenciador_loja_backend.models.Pedido;
import com.example.gerenciador_loja_backend.services.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Criar pedido
    @PostMapping("/pedidos")
    public ResponseEntity<Pedido> criarPedido(@RequestBody PedidoDto pedidoDto) {
        Pedido pedido = pedidoService.criarPedido(pedidoDto);
        return ResponseEntity.ok(pedido);
    }

    // Listar todos os pedidos
    @GetMapping("/pedidos")
    public ResponseEntity<List<Pedido>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.getAllPedidos();
        return ResponseEntity.ok(pedidos);
    }

    // Buscar pedido por ID
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<Object> buscarPedido(@PathVariable UUID id) {
        return pedidoService.getOnePedido(id);
    }

    // Editar pedido
    @PutMapping("/pedidos/{id}")
    public ResponseEntity<Object> editarPedido(@PathVariable UUID id, @RequestBody PedidoDto pedidoDto) {
        return pedidoService.editPedido(id, pedidoDto);
    }

    // Deletar pedido
    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity<Object> deletarPedido(@PathVariable UUID id) {
        return pedidoService.deletePedido(id);
    }
}
