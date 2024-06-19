package com.example.demo.repositories;

import com.example.demo.entities.DemandaPronosticada;
import java.util.List;

public interface DemandaPronosticadaRepository extends BaseRepository<DemandaPronosticada, Long> {
    List<DemandaPronosticada> findAllByPronosticoId(Long id);

}