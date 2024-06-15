package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.Modelo;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.ModeloRepository;
import com.example.demo.repositories.ProveedorArticuloRepository;
@Service
public class ModeloServiceImpl extends BaseServiceImpl<Modelo, Long> implements ModeloService{
    @Autowired
    private ModeloRepository modeloRepository;

    public ModeloServiceImpl(BaseRepository<Modelo, Long> baseRepository, ModeloRepository modeloRepository) {
        super(baseRepository);
        this.modeloRepository=modeloRepository;
    }

}
