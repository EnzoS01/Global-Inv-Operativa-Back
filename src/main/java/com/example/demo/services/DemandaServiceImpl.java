package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.VentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.DemandaRepository;

import java.util.ArrayList;
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
    public Demanda setDetallesVenta(Long demandaId){
        Demanda demanda = demandaRepository.findById(demandaId)
                .orElseThrow(() -> new RuntimeException("Demanda no encontrada"));

        //Genero al fecha de inicio y fin con el año y numPeriodo de la demanda, para encontrar todas las ventas que se encuentren en ese rango de fechas;
        Date fechaInicio= new Date(demanda.getAño(),demanda.getNumPeriodo(),1 );
        Date fechaFin=new Date(demanda.getAño(),demanda.getNumPeriodo(),31 );

        List<Venta> ventas= ventaRepository.findByFecha(fechaInicio,fechaFin);

        List<Venta> ventasRevisar= new ArrayList<>();
        //Por cada venta encontrada entre las fechas correspondientes, se busca que el detalle tenga el mismo artículo que la demanda para poder agregar el detalle a la demanda;
        for (Venta venta:ventas){
            List<DetalleVenta> detallesVenta=venta.getDetallesVenta();
            for (DetalleVenta detalleVenta:detallesVenta){
                if(demanda.getArticulo()==detalleVenta.getArticulo()){
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
