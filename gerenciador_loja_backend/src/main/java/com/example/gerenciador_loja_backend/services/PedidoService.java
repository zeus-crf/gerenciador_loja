package com.example.gerenciador_loja_backend.services;

import com.example.gerenciador_loja_backend.dtos.PedidoDto;
import com.example.gerenciador_loja_backend.models.Cliente;
import com.example.gerenciador_loja_backend.models.Pedido;
import com.example.gerenciador_loja_backend.repositories.ClienteRepository;
import com.example.gerenciador_loja_backend.repositories.PedidoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
    }


    public Pedido criarPedido(PedidoDto pedidoDto){
        UUID clienteId = pedidoDto.idCliente();
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setItensList(pedidoDto.itensList());
        pedido.setStatusDePagamento(pedidoDto.statusDePagamento());
        pedido.setParcelasRestantes(pedidoDto.parcelasRestantes());

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> getAllPedidos(){
        return pedidoRepository.findAll();
    }

    public ResponseEntity<Object> getOnePedido(UUID id){
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if (pedido.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(pedidoRepository.findById(id));
    }

    public ResponseEntity<Object> editPedido(UUID id, PedidoDto pedidoDto){
        Optional<Pedido> pedido0 = pedidoRepository.findById(id);
        if (pedido0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Pedido pedido = pedido0.get();

        // Atualiza campos
        Cliente cliente = clienteRepository.findById(pedidoDto.idCliente())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        pedido.setCliente(cliente);
        pedido.setItensList(pedidoDto.itensList());
        pedido.setStatusDePagamento(pedidoDto.statusDePagamento());
        pedido.setParcelasRestantes(pedidoDto.parcelasRestantes());

        return ResponseEntity.ok(pedidoRepository.save(pedido));
    }


    public ResponseEntity<Object> deletePedido(UUID id){
        Optional<Pedido> pedido0 = pedidoRepository.findById(id);
        if (pedido0.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.ok("Pedido excluído com sucesso");
    }

}
