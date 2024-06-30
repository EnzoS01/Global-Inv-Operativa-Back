package com.example.demo.repositories;

import com.example.demo.entities.ModeloPrediccion;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeloPrediccionRepository extends BaseRepository<ModeloPrediccion,Long>{
    ModeloPrediccion findBynombreModelo(String nombreModelo);
}
