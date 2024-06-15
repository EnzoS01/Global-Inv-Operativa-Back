package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.Proveedor;
import com.example.demo.entities.ProveedorArticulo;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.ProveedorArticuloRepository;
@Service
public class ProveedorArticuloServiceImpl extends BaseServiceImpl<ProveedorArticulo,Long> implements ProveedorArticuloService{
    @Autowired
    private ProveedorArticuloRepository proveedorArticuloRepository;

    public ProveedorArticuloServiceImpl(BaseRepository<ProveedorArticulo,Long> baseRepository, ProveedorArticuloRepository proveedorArticuloRepository){
        super(baseRepository);
        this.proveedorArticuloRepository = proveedorArticuloRepository;
    }
}



