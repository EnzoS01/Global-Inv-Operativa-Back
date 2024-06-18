package com.example.demo.services;

import com.example.demo.entities.Demanda;
import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.entities.Pronostico;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.DemandaPronosticadaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PronosticoServiceImpl extends BaseServiceImpl<Pronostico, Long> implements PronosticoService {

    private final DemandaPronosticadaRepository demandaPronosticadaRepository;

    @Autowired
    public PronosticoServiceImpl(BaseRepository<Pronostico, Long> baseRepository, DemandaPronosticadaRepository demandaPronosticadaRepository) {
        super(baseRepository);
        this.demandaPronosticadaRepository = demandaPronosticadaRepository;
    }

    @Override
    public List<Demanda> seleccionarDemandaHistoricas(List<Demanda> listaDemandasHistoricas, int nroAnio) {
        return listaDemandasHistoricas.stream()
                .filter(demanda -> demanda.getAnio() == nroAnio)
                .collect(Collectors.toList());
    }

    @Override
    public List<DemandaPronosticada> promedioMovil(List<Demanda> demandasHistoricasSeleccionadas) {
        return List.of();
    }

    @Override
    public List<DemandaPronosticada> promedioPonderado(List<Demanda> demandasHistoricasSeleccionadas) {
        return List.of();
    }

    @Override
    public List<DemandaPronosticada> suavizacionExponencial(List<Demanda> demandasHistoricasSeleccionadas) {
        return List.of();
    }

    @Override
    public List<DemandaPronosticada> promedioMovil(List<Demanda> demandasHistoricasSeleccionadas, int n, int cantidadPeriodosAPredecir) {
        List<DemandaPronosticada> demandaPronosticada3 = new ArrayList<>();
        int periodoInicioPredecir = demandasHistoricasSeleccionadas.get(0).getNumPeriodo() + n;

        for (int i = 0; i < cantidadPeriodosAPredecir; i++) {
            DemandaPronosticada dp = new DemandaPronosticada();
            dp.setNroPeriodoDemandaPronosticada(periodoInicioPredecir + i);

            double suma = 0;
            int indiceFin = i + n - 1;

            for (int j = i; j <= indiceFin; j++) {
                if (j < demandasHistoricasSeleccionadas.size()) {
                    suma += demandasHistoricasSeleccionadas.get(j).getCantTotalDemanda();
                }
            }

            double promedioMovil = suma / n;
            dp.setCantidadDemandadaPronostico(promedioMovil);
            dp.setAnioDemandaPronosticada(demandasHistoricasSeleccionadas.get(0).getAnio());
            dp.setNroPeriodoDemandaPronosticada(periodoInicioPredecir + i + n);
            if ((periodoInicioPredecir + i + n - 1) < demandasHistoricasSeleccionadas.size()) {
                dp.setDemandaRealAsociada(demandasHistoricasSeleccionadas.get(periodoInicioPredecir + i + n - 1));
            }
            demandaPronosticada3.add(dp);
        }
        return demandaPronosticada3;
    }

    @Override
    public List<DemandaPronosticada> promedioPonderado(List<Demanda> demandasHistoricasSeleccionadas, int n, int cantidadPeriodosAPredecir) {
        List<DemandaPronosticada> demandaPronosticada = new ArrayList<>();
        double[] ponderaciones = calcularPonderaciones(n);

        if (demandasHistoricasSeleccionadas.size() < n) {
            throw new IllegalArgumentException("No hay suficientes datos históricos para realizar el cálculo.");
        }

        int periodoInicioPredecir = demandasHistoricasSeleccionadas.get(0).getNumPeriodo() + 1 - n;

        for (int i = 0; i < cantidadPeriodosAPredecir; i++) {
            double sumaPonderada = 0;

            for (int j = 0; j < n; j++) {
                int indiceHistorico = i + j;
                sumaPonderada += demandasHistoricasSeleccionadas.get(indiceHistorico).getCantTotalDemanda() * ponderaciones[j];
            }

            DemandaPronosticada dp = new DemandaPronosticada();
            dp.setNroPeriodoDemandaPronosticada(periodoInicioPredecir + i + n);
            dp.setAnioDemandaPronosticada(demandasHistoricasSeleccionadas.get(0).getAnio());
            dp.setCantidadDemandadaPronostico(sumaPonderada);
            if ((periodoInicioPredecir + i + n - 1) < demandasHistoricasSeleccionadas.size()) {
                dp.setDemandaRealAsociada(demandasHistoricasSeleccionadas.get(periodoInicioPredecir + i + n - 1));
            }
            demandaPronosticada.add(dp);
        }

        return demandaPronosticada;
    }

    @Override
    public List<DemandaPronosticada> suavizacionExponencial(List<Demanda> demandasHistoricasSeleccionadas, double alpha, int mesDemandaAnterior, int cantidadRealDemandadaAnterior, int cantidadDemandadaPronosticadaAnterior) {
        List<DemandaPronosticada> demandasPronosticadas = new ArrayList<>();

        double prediccionSuavizacionExponencial = cantidadDemandadaPronosticadaAnterior
                + alpha * (cantidadRealDemandadaAnterior - cantidadDemandadaPronosticadaAnterior);

        DemandaPronosticada dp = new DemandaPronosticada();
        dp.setAnioDemandaPronosticada(demandasHistoricasSeleccionadas.get(0).getAnio());
        dp.setCantidadDemandadaPronostico(prediccionSuavizacionExponencial);
        dp.setNroPeriodoDemandaPronosticada(mesDemandaAnterior + 1);
        dp.setDemandaRealAsociada(demandasHistoricasSeleccionadas.get(mesDemandaAnterior - 1));
        demandasPronosticadas.add(dp);

        return demandasPronosticadas;
    }

    @Override
    public List<DemandaPronosticada> pronosticoEstacional(List<Demanda> demandasHistoricas) {
        // Implementación no proporcionada en el ejemplo original
        return new ArrayList<>();
    }

    @Override
    public List<DemandaPronosticada> predecirDemanda(List<Demanda> demandas) {
        // Implementación no proporcionada en el ejemplo original
        return new ArrayList<>();
    }

    @Override
    public DemandaPronosticada getPronostico(Long id) {
        Optional<DemandaPronosticada> pronosticoOpt = demandaPronosticadaRepository.findById(id);
        return pronosticoOpt.orElse(null);
    }

    @Override
    public DemandaPronosticada updatePronostico(Long id, DemandaPronosticada pronosticoActualizado) {
        if (!demandaPronosticadaRepository.existsById(id)) {
            throw new IllegalArgumentException("El pronóstico no existe.");
        }
        pronosticoActualizado.setId(id);
        return demandaPronosticadaRepository.save(pronosticoActualizado);
    }

    @Override
    public void deletePronostico(Long id) {
        if (!demandaPronosticadaRepository.existsById(id)) {
            throw new IllegalArgumentException("El pronóstico no existe.");
        }
        demandaPronosticadaRepository.deleteById(id);
    }

    @Override
    public List<DemandaPronosticada> getAllPronosticos() {
        return demandaPronosticadaRepository.findAll();
    }

    private double[] calcularPonderaciones(int n) {
        double[] ponderaciones = new double[n];
        double sumaPonderaciones = 0.0;

        for (int i = 0; i < n; i++) {
            ponderaciones[i] = (double) (n - i) / (n * (n + 1) / 2.0);
            sumaPonderaciones += ponderaciones[i];
        }

        for (int i = 0; i < n; i++) {
            ponderaciones[i] /= sumaPonderaciones;
        }

        return ponderaciones;
    }
}
