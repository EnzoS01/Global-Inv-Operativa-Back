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

    private ArticuloRepository articuloRepository;

    public DetalleVentaServiceImpl(BaseRepository<DetalleVenta, Long> baseRepository, DetalleVentaRepository detalleVentaRepository) {
        super(baseRepository);
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Transactional
    private void agregarArticulo(Long detalleVentaId, Articulo articulo){
        DetalleVenta detalleVenta = detalleVentaRepository.findById(detalleVentaId).orElseThrow(() -> new RuntimeException("Detalle de Venta no encontrado"));
        detalleVenta.setArticulo(articulo);
        int cantidad=detalleVenta.getCantidad();
        articulo.setCantActual(articulo.getCantActual()-cantidad);
        detalleVentaRepository.save(detalleVenta);
        articuloRepository.save(articulo);
    }
}
