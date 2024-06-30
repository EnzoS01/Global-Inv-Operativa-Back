package com.example.demo.services;

import com.example.demo.entities.ModeloPrediccion;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.ModeloPrediccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModeloPrediccionServiceImpl extends BaseServiceImpl<ModeloPrediccion,Long> implements ModeloPrediccionService{

    @Autowired
    private ModeloPrediccionRepository modeloPrediccionRepository;

    public ModeloPrediccionServiceImpl(BaseRepository<ModeloPrediccion, Long> baseRepository, ModeloPrediccionRepository modeloPrediccionRepository) {
        super(baseRepository);
        this.modeloPrediccionRepository = modeloPrediccionRepository;
    }
}
