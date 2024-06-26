package com.example.demo.services;

import com.example.demo.entities.DemandaPronosticada;

public interface DemandaPronosticadaService extends BaseService<DemandaPronosticada, Long> {
    public DemandaPronosticada asignarDemanda(Long demandaPronosticadaId, Long demandaId) throws Exception;

}
