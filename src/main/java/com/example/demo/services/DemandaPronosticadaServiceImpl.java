package com.example.demo.services;

import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.repositories.DemandaPronosticadaRepository;
import org.springframework.stereotype.Service;

@Service
public class DemandaPronosticadaServiceImpl extends BaseServiceImpl<DemandaPronosticada, Long> implements DemandaPronosticadaService {

    public DemandaPronosticadaServiceImpl(DemandaPronosticadaRepository demandaPronosticadaRepository) {
        super(demandaPronosticadaRepository);
    }
}