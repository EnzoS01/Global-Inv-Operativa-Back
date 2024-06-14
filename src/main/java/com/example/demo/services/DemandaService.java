package com.example.demo.services;

import com.example.demo.entities.Demanda;

public interface DemandaService extends BaseService<Demanda,Long>{
    public Demanda setArticulo(Long demandaId, Long articuloId) throws Exception;
    public Demanda setDetallesVenta(Long demandaId) throws Exception;
    public Demanda calcularTotalDemandada(Long demandaId) throws Exception;
}
