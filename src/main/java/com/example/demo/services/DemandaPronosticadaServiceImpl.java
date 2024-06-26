package com.example.demo.services;

import com.example.demo.entities.Cliente;
import com.example.demo.entities.Demanda;
import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.entities.Venta;
import com.example.demo.repositories.DemandaPronosticadaRepository;
import com.example.demo.repositories.DemandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemandaPronosticadaServiceImpl extends BaseServiceImpl<DemandaPronosticada, Long> implements DemandaPronosticadaService {

    @Autowired
    private DemandaPronosticadaRepository demandaPronosticadaRepository;
    @Autowired
    private DemandaRepository demandaRepository;

    public DemandaPronosticadaServiceImpl(DemandaPronosticadaRepository demandaPronosticadaRepository) {
        super(demandaPronosticadaRepository);
    }

    /*
    public DemandaPronosticada asignarDemanda(Long demandaPronosticadaId, Long demandaId){
        DemandaPronosticada demandaPronosticada= demandaPronosticadaRepository.findById(demandaPronosticadaId)
                .orElseThrow(() -> new RuntimeException("DemandaPronosticada no encontrada"));
        Demanda demanda= demandaRepository.findById(demandaId)
                .orElseThrow(() -> new RuntimeException("Demanda no encontrada"));
        //Setea la demanda real
        demandaPronosticada.setDemandaRealAsociada(demanda);

        //Calcula el error
        demandaPronosticada.setCantidadDemandaReal(demanda.getCantTotalDemanda());
        double error;
        if ((demanda.getCantTotalDemanda()-demandaPronosticada.getCantidadDemandadaPronostico())<0){
            error=(demanda.getCantTotalDemanda()-demandaPronosticada.getCantidadDemandadaPronostico())*(-1);
        }else{
            error=(demanda.getCantTotalDemanda()-demandaPronosticada.getCantidadDemandadaPronostico());
        }
        demandaPronosticada.setValorErrorPronosticoDemandaPronosticada(error);

        demandaPronosticadaRepository.save(demandaPronosticada);
        return demandaPronosticada;
    }

*/


}