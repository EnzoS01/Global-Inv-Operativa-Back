package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.VentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.DemandaRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DemandaServiceImpl extends BaseServiceImpl<Demanda,Long> implements DemandaService{
    
    @Autowired
    private DemandaRepository demandaRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private VentaRepository ventaRepository;

    public DemandaServiceImpl(BaseRepository<Demanda, Long> baseRepository, DemandaRepository demandaRepository) {
        super(baseRepository);
        this.demandaRepository=demandaRepository;
    }

    @Transactional
    public Demanda setArticulo(Long demandaId, Long articuloId){
        Demanda demanda = demandaRepository.findById(demandaId)
                .orElseThrow(() -> new RuntimeException("Demanda no encontrada"));
        Articulo articulo = articuloRepository.findById(articuloId)
                .orElseThrow(() -> new RuntimeException("Articulo no encontrado"));

        demanda.setArticulo(articulo);
        demandaRepository.save(demanda);
        return demanda;
    }

    @Transactional
    public Demanda setDetallesVenta(Long demandaId) {
        Demanda demanda = demandaRepository.findById(demandaId)
                .orElseThrow(() -> new RuntimeException("Demanda no encontrada"));

        // Genero la fecha de inicio y fin con el año y numPeriodo de la demanda
        LocalDate fechaInicio = LocalDate.of(demanda.getAnio(), demanda.getNumPeriodo(), 1);
        System.out.println("Esta es la fecha de inicio generada: " + fechaInicio);

        LocalDate fechaFin = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth());
        System.out.println("Esta es la fecha de fin generada: " + fechaFin);

        // Convertir LocalDate a Date
        Date startDate = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Venta> ventas = ventaRepository.findByfechaRealizacionBetween(startDate, endDate);

        // Por cada venta encontrada entre las fechas correspondientes, se busca que el detalle tenga el mismo artículo que la demanda para poder agregar el detalle a la demanda;
        for (Venta venta : ventas) {
            List<DetalleVenta> detallesVenta = venta.getDetallesVenta();
            for (DetalleVenta detalleVenta : detallesVenta) {
                if (demanda.getArticulo().equals(detalleVenta.getArticulo())) {
                    demanda.addDetallesVenta(detalleVenta);
                }
            }
        }

        demandaRepository.save(demanda);
        return demanda;
    }

    @Transactional
    public Demanda calcularTotalDemandada(Long demandaId){
        Demanda demanda = demandaRepository.findById(demandaId)
                .orElseThrow(() -> new RuntimeException("Demanda no encontrada"));
        List<DetalleVenta> detalleVentas= demanda.getDetallesVenta();
        int total= 0;
        for (DetalleVenta detalleVenta: detalleVentas){
            total= total + detalleVenta.getCantidad();
        }
        demanda.setCantTotalDemanda(total);
        demandaRepository.save(demanda);
        return demanda;
    }

}
