package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Articulo;

public interface ArticuloService extends BaseService<Articulo,Long>{
    List<Articulo> findProductosAReponer() throws Exception;
}
