package com.example.demo.services;

import com.example.demo.entities.OrdenCompra;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.OrdenCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdenCompraServiceImpl extends BaseServiceImpl<OrdenCompra,Long> implements OrdenCompraService {
    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    public OrdenCompraServiceImpl(BaseRepository<OrdenCompra, Long> baseRepository, OrdenCompraRepository ordenCompraRepository) {
        super(baseRepository);
        this.ordenCompraRepository = ordenCompraRepository;
    }
}
