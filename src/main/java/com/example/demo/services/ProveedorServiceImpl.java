package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Proveedor;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.ProveedorRepository;
@Service
public class ProveedorServiceImpl extends BaseServiceImpl<Proveedor,Long> implements ProveedorService{

    private ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(BaseRepository<Proveedor,Long> baseRepository, ProveedorRepository proveedorRepository) {
        super(baseRepository);
        this.proveedorRepository= proveedorRepository;
    }
    
}



