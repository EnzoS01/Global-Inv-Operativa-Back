package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.ArticuloDTO;
import com.example.demo.entities.Articulo;
import com.example.demo.entities.EstadoOrdenCompra;
import com.example.demo.entities.OrdenCompra;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.EstadoOrdenCompraRepository;
@Service
public class EstadoOrdenCompraServiceImpl extends BaseServiceImpl<EstadoOrdenCompra, Long> implements EstadoOrdenCompraService{

    @Autowired
    EstadoOrdenCompraRepository estadoOrdenCompraRepository;

    public EstadoOrdenCompraServiceImpl(BaseRepository<EstadoOrdenCompra, Long> baseRepository, EstadoOrdenCompraRepository estadoOrdenCompraRepository) {
        super(baseRepository);
        this.estadoOrdenCompraRepository=estadoOrdenCompraRepository;
    }
}