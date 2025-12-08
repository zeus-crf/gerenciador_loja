package com.example.gerenciador_loja_backend.models;

import com.example.gerenciador_loja_backend.enuns.StatusDePagamento;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_PEDIDOS")
public class Pedido extends RepresentationModel<Pedido> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ElementCollection
    @CollectionTable(name = "pedido_itens", joinColumns = @JoinColumn(name = "pedido_id"))
    @Column(name = "item")
    private List<String> itensList;

    @Enumerated(EnumType.STRING)
    private StatusDePagamento statusDePagamento;

    private Integer parcelasRestantes;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<String> getItensList() {
        return itensList;
    }

    public void setItensList(List<String> itensList) {
        this.itensList = itensList;
    }

    public StatusDePagamento getStatusDePagamento() {
        return statusDePagamento;
    }

    public void setStatusDePagamento(StatusDePagamento statusDePagamento) {
        this.statusDePagamento = statusDePagamento;
    }

    public Integer getParcelasRestantes() {
        return parcelasRestantes;
    }

    public void setParcelasRestantes(Integer parcelasRestantes) {
        this.parcelasRestantes = parcelasRestantes;
    }
}
