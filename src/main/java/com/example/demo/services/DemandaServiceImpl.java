package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Demanda;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.DemandaRepository;

@Service
public class DemandaServiceImpl extends BaseServiceImpl<Demanda,Long> implements DemandaService{
    
    @Autowired
    private DemandaRepository demandaRepository;

    public DemandaServiceImpl(BaseRepository<Demanda, Long> baseRepository, DemandaRepository demandaRepository) {
        super(baseRepository);
        this.demandaRepository=demandaRepository;
    }

}
