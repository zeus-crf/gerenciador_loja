package com.example.gerenciador_loja_backend.controllers;

import com.example.gerenciador_loja_backend.dtos.PedidoDto;
import com.example.gerenciador_loja_backend.models.Pedido;
import com.example.gerenciador_loja_backend.services.PedidoService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }


    @PostMapping("/pedidos")
    public ResponseEntity<Object> criarPedido(@RequestBody PedidoDto pedidoDto) {
        return pedidoService.criarPedido(pedidoDto);
    }


    @GetMapping("/pedidos")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> listarPedidos() {

        List<Pedido> pedidos = pedidoService.getAllPedidos();

        List<EntityModel<Pedido>> pedidoModels = pedidos.stream()
                .map(pedido -> EntityModel.of(
                        pedido,
                        linkTo(methodOn(PedidoController.class).buscarPedido(pedido.getId())).withSelfRel()
                ))
                .toList();

        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(
                pedidoModels,
                linkTo(methodOn(PedidoController.class).listarPedidos()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }



    @GetMapping("/pedidos/{id}")
    public ResponseEntity<Object> buscarPedido(@PathVariable UUID id) {
        return pedidoService.getOnePedido(id);
    }


    @PutMapping("/pedidos/{id}")
    public ResponseEntity<Object> editarPedido(@PathVariable UUID id, @RequestBody PedidoDto pedidoDto) {
        return pedidoService.editPedido(id, pedidoDto);
    }


    @PutMapping("/pedidos/{id}/diminuir-parcela")
    public ResponseEntity<Object> diminuirParcela(@PathVariable UUID id) {
        return pedidoService.diminuirParcela(id);
    }


    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity<Object> deletarPedido(@PathVariable UUID id) {
        return pedidoService.deletePedido(id);
    }
}
