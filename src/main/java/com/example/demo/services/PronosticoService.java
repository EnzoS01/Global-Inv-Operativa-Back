package com.example.demo.services;

import com.example.demo.entities.Demanda;
import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.entities.Pronostico;

import java.util.List;

public interface PronosticoService extends BaseService<Pronostico, Long> {
    List<Demanda> seleccionarDemandaHistoricas(List<Demanda> listaDemandasHistoricas, int nroAnio);

    List<DemandaPronosticada> promedioMovil(List<Demanda> demandasHistoricasSeleccionadas, int n,
            int cantidadPeriodosAPredecir);

    List<DemandaPronosticada> promedioPonderado(List<Demanda> demandasHistoricasSeleccionadas, int n,
            int cantidadPeriodosAPredecir);

    List<DemandaPronosticada> suavizacionExponencial(List<Demanda> demandasHistoricasSeleccionadas, double alpha,
            int mesDemandaAnterior, int cantidadRealDemandadaAnterior, int cantidadDemandadaPronosticadaAnterior);

    List<DemandaPronosticada> pronosticoEstacional(List<Demanda> demandasHistoricas, int anioAPredecir, int anioInicio,
            int anioFin, int demandaEsperada);

    List<DemandaPronosticada> predecirDemanda(List<Demanda> demandas);

}