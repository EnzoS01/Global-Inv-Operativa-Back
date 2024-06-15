package com.example.demo.repositories;

import com.example.demo.entities.DemandaPronosticada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandaPronosticadaRepository extends JpaRepository<DemandaPronosticada, Long> {
    List<DemandaPronosticada> findByAnioDemandaPronosticadaAndMesDemandaPronosticada(int anio, int mes);

    List<DemandaPronosticada> findByAnioDemandaPronosticada(int anio);

    // Otros métodos de consulta personalizados según tus necesidades
}
