package com.example.gerenciador_loja_backend.services;

import com.example.gerenciador_loja_backend.dtos.ItemPedidoDto;
import com.example.gerenciador_loja_backend.dtos.PedidoDto;
import com.example.gerenciador_loja_backend.enuns.StatusDePagamento;
import com.example.gerenciador_loja_backend.models.Cliente;
import com.example.gerenciador_loja_backend.models.ItemPedido;
import com.example.gerenciador_loja_backend.models.Pedido;
import com.example.gerenciador_loja_backend.repositories.ClienteRepository;
import com.example.gerenciador_loja_backend.repositories.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
    }

    // ============================================================
    // CRIAR PEDIDO
    // ============================================================
    public ResponseEntity<Object> criarPedido(PedidoDto dto) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(dto.idCliente());
        if (clienteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }
        Cliente cliente = clienteOptional.get();

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setParcelasTotais(dto.parcelasTotais());
        pedido.setParcelasRestantes(dto.parcelasTotais());
        pedido.setStatusDePagamento(StatusDePagamento.PENDENTE);

        // converter ItemPedidoDto -> ItemPedido e setar o pedido em cada item
        List<ItemPedido> itens = dto.itens().stream().map(i -> {
            ItemPedido item = new ItemPedido();
            item.setNomeProduto(i.nome());
            item.setPrecoUnitario(i.preco());
            item.setQuantidade(i.quantidade());
            item.setTamanho(i.tamanho());
            item.setPedido(pedido);
            return item;
        }).collect(Collectors.toList());

        pedido.setItens(itens);

        // Calcula o valor total
        calcularValorTotal(pedido);

        //calcular o valor da parcela
        calcularValorParcela(pedido);

        Pedido salvo = pedidoRepository.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // ============================================================
    // LISTAR TODOS OS PEDIDOS
    // ============================================================
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    // ============================================================
    // BUSCAR PEDIDO POR ID
    // ============================================================
    public ResponseEntity<Object> getOnePedido(UUID id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if (pedido.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        }
        return ResponseEntity.ok(pedido.get());
    }

    // ============================================================
    // EDITAR PEDIDO
    // ============================================================
    public ResponseEntity<Object> editPedido(UUID id, PedidoDto dto) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        }

        Pedido pedido = pedidoOpt.get();

        // Atualiza itens
        if (dto.itens() != null) {
            for (ItemPedidoDto itemDto : dto.itens()) {
                if (itemDto.id() != null) {
                    // Atualiza item existente
                    pedido.getItens().stream()
                            .filter(i -> i.getId().equals(itemDto.id()))
                            .findFirst()
                            .ifPresent(i -> {
                                if (itemDto.nome() != null) i.setNomeProduto(itemDto.nome());
                                if (itemDto.preco() != null) i.setPrecoUnitario(itemDto.preco());
                                if (itemDto.quantidade() != null) i.setQuantidade(itemDto.quantidade());
                                if (itemDto.tamanho() != null) i.setTamanho(itemDto.tamanho());
                            });
                } else {
                    // Cria novo item
                    ItemPedido novoItem = new ItemPedido();
                    novoItem.setNomeProduto(itemDto.nome());
                    novoItem.setPrecoUnitario(itemDto.preco());
                    novoItem.setQuantidade(itemDto.quantidade());
                    novoItem.setTamanho(itemDto.tamanho());
                    novoItem.setPedido(pedido);
                    pedido.getItens().add(novoItem);
                }
            }

            calcularValorTotal(pedido); // recalcula valor total
            calcularValorParcela(pedido); // recalcula o valor da parcela
        }

        // Atualiza parcelas totais
        if (dto.parcelasTotais() != null) {
            if (dto.parcelasTotais() < pedido.getParcelasTotais()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("As parcelas totais não podem ser reduzidas.");
            }
            pedido.setParcelasTotais(dto.parcelasTotais());
        }

        // Atualiza parcelas restantes
        if (dto.parcelasRestantes() != null) {
            if (dto.parcelasRestantes() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Parcelas restantes não podem ser negativas");
            }
            if (dto.parcelasRestantes() > pedido.getParcelasTotais()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Parcelas restantes não podem ser maiores que o total");
            }
            pedido.setParcelasRestantes(dto.parcelasRestantes());
        }

        atualizarStatus(pedido);

        return ResponseEntity.ok(pedidoRepository.save(pedido));
    }



    // ============================================================
    // DIMINUIR 1 PARCELA
    // ============================================================
    public ResponseEntity<Object> diminuirParcela(UUID id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        }

        Pedido pedido = pedidoOpt.get();

        if (pedido.getParcelasRestantes() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Não há parcelas restantes para diminuir");
        }

        pedido.setParcelasRestantes(pedido.getParcelasRestantes() - 1);
        atualizarStatus(pedido);

        return ResponseEntity.ok(pedidoRepository.save(pedido));
    }

    // ============================================================
    // FILTRAR PEDIDOS POR STATUS
    // ============================================================
    public ResponseEntity<Object> getByStatus(StatusDePagamento status) {
        return ResponseEntity.ok(pedidoRepository.findByStatusDePagamento(status));
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================
    private void atualizarStatus(Pedido pedido) {
        if (pedido.getParcelasRestantes() == 0) {
            pedido.setStatusDePagamento(StatusDePagamento.PAGO);
        } else {
            pedido.setStatusDePagamento(StatusDePagamento.PENDENTE);
        }
    }

    private void calcularValorTotal(Pedido pedido) {
        double total = pedido.getItens().stream()
                .mapToDouble(i -> i.getPrecoUnitario() * i.getQuantidade())
                .sum();
        pedido.setValorTotal(total);
    }

    private void calcularValorParcela(Pedido pedido){
        double parcela = pedido.getValorTotal() / pedido.getParcelasTotais();
        pedido.setValorParcelas(parcela);
    }

    // ============================================================
    // EXCLUIR PEDIDO
    // ============================================================
    public ResponseEntity<Object> deletePedido(UUID id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if (pedido.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.ok("Pedido excluído com sucesso");
    }
}
