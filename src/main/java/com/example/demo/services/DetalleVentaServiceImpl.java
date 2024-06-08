package com.example.demo.services;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.DetalleVenta;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.DetalleVentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleVentaServiceImpl extends BaseServiceImpl<DetalleVenta,Long> implements DetalleVentaService {
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    @Autowired
    private ArticuloRepository articuloRepository;

    public DetalleVentaServiceImpl(BaseRepository<DetalleVenta, Long> baseRepository, DetalleVentaRepository detalleVentaRepository) {
        super(baseRepository);
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Transactional
    public DetalleVenta agregarArticulo(Long detalleVentaId, Articulo articulo){
        DetalleVenta detalleVenta = detalleVentaRepository.findById(detalleVentaId).orElseThrow(() -> new RuntimeException("Detalle de Venta no encontrado"));
        articulo.setCantActual(articulo.getCantActual()-detalleVenta.getCantidad());
        articuloRepository.save(articulo);
        detalleVenta.setArticulo(articulo);
        detalleVentaRepository.save(detalleVenta);
        return detalleVenta;
    }
}
