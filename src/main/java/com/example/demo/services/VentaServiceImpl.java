package com.example.demo.services;

import com.example.demo.entities.Cliente;
import com.example.demo.entities.DetalleVenta;
import com.example.demo.entities.Venta;
import com.example.demo.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaServiceImpl extends BaseServiceImpl<Venta,Long> implements VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
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
        ventaRepository.save(venta);
        return venta;
    }

    @Transactional
    public Venta calcularTotal(Long ventaId){
        Venta venta= ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        List<DetalleVenta> detalleVentas= venta.getDetallesVenta();
        double total= 0;
        for (DetalleVenta detalleVenta: detalleVentas){
            total= total + detalleVenta.getSubtotal();
        }
        venta.setTotal(total);
        ventaRepository.save(venta);
        return venta;
    }

    @Transactional
    public Venta agregarCliente(Long ventaId, Long clienteId){
        Venta venta= ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        Cliente cliente= clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        venta.setCliente(cliente);
        ventaRepository.save(venta);
        return venta;
    }
}
