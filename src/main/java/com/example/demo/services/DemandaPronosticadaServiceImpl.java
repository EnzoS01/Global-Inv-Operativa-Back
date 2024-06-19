package com.example.demo.services;

import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.repositories.DemandaPronosticadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemandaPronosticadaServiceImpl extends BaseServiceImpl<DemandaPronosticada, Long> implements DemandaPronosticadaService {

    @Autowired
    public DemandaPronosticadaServiceImpl(DemandaPronosticadaRepository demandaPronosticadaRepository) {
        super(demandaPronosticadaRepository);
    }
}