package com.example.demo.services;

import com.example.demo.entities.Articulo;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo, Long> {
    @Autowired
    private ArticuloRepository articuloRepository;

    public ArticuloServiceImpl(BaseRepository baseRepository, ArticuloRepository articuloRepository) {
        super(baseRepository);
        this.articuloRepository = articuloRepository;
    }
}
