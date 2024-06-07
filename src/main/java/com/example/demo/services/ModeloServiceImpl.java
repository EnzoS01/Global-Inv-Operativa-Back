package com.example.demo.services;

import com.example.demo.entities.Modelo;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.ModeloRepository;
import org.springframework.stereotype.Service;

@Service
public class ModeloServiceImpl extends BaseServiceImpl<Modelo,Long> implements ModeloService {
    private ModeloRepository modeloRepository;

    public ModeloServiceImpl(BaseRepository<Modelo, Long> baseRepository, ModeloRepository modeloRepository) {
        super(baseRepository);
        this.modeloRepository = modeloRepository;
    }
}
