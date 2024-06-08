package com.example.demo.services;

import com.example.demo.entities.Cliente;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl extends BaseServiceImpl<Cliente, Long> implements ClienteService {
    private ClienteRepository clienteRepository;

    public ClienteServiceImpl(BaseRepository<Cliente, Long> baseRepository, ClienteRepository clienteRepository) {
        super(baseRepository);
        this.clienteRepository = clienteRepository;
    }
}
