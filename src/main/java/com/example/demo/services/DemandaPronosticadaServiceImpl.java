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


}