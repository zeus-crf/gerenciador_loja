package com.example.gerenciador_loja_backend.services;

import com.example.gerenciador_loja_backend.dtos.ClienteDto;
import com.example.gerenciador_loja_backend.models.Cliente;
import com.example.gerenciador_loja_backend.repositories.ClienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Criar cliente
    public Cliente criarCliente(ClienteDto clienteDto) {
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteDto, cliente);
        return clienteRepository.save(cliente);
    }

    // Listar todos os clientes
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // Buscar cliente por ID
    public Optional<Cliente> buscarClientePorId(UUID id) {
        return clienteRepository.findById(id);
    }

    // Atualizar cliente
    public Optional<Cliente> atualizarCliente(UUID id, ClienteDto clienteDto) {
        Optional<Cliente> clienteO = clienteRepository.findById(id);
        if (clienteO.isEmpty()) {
            return Optional.empty();
        }
        Cliente cliente = clienteO.get();
        BeanUtils.copyProperties(clienteDto, cliente);
        return Optional.of(clienteRepository.save(cliente));
    }

    // Deletar cliente
    public boolean deletarCliente(UUID id) {
        Optional<Cliente> clienteO = clienteRepository.findById(id);
        if (clienteO.isEmpty()) {
            return false;
        }
        clienteRepository.deleteById(id);
        return true;
    }
}
