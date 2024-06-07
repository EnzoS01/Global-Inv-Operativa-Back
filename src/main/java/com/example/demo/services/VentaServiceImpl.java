package com.example.demo.services;

import com.example.demo.entities.Venta;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaServiceImpl extends BaseServiceImpl<Venta,Long> implements VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    public VentaServiceImpl(BaseRepository<Venta, Long> baseRepository, VentaRepository ventaRepository) {
        super(baseRepository);
        this.ventaRepository = ventaRepository;
    }


    public List<Venta> ObtenerTodas(){
        List<Venta> ventas = ventaRepository.findAll();
        return ventas;
    }



}
