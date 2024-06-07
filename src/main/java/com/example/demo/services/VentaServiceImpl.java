package com.example.demo.services;

import com.example.demo.entities.DetalleVenta;
import com.example.demo.entities.Venta;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.DetalleVentaRepository;
import com.example.demo.repositories.VentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaServiceImpl extends BaseServiceImpl<Venta,Long> implements VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    private DetalleVentaRepository detalleVentaRepository;

    public VentaServiceImpl(BaseRepository<Venta, Long> baseRepository, VentaRepository ventaRepository) {
        super(baseRepository);
        this.ventaRepository = ventaRepository;
    }

    /* REDUNDANTE ya lo hace getAll
    public List<Venta> ObtenerTodas(){
        List<Venta> ventas = ventaRepository.findAll();
        return ventas;

    }*/

    @Transactional
    public Venta agregarDetalleVenta(Long ventaid, DetalleVenta detalleVenta){
        Venta venta= ventaRepository.findById(ventaid).orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        venta.addDetallesVenta(detalleVenta);
        detalleVentaRepository.save(detalleVenta);
        return ventaRepository.save(venta);
    }

}
