package com.example.demo.services;

import com.example.demo.entities.Demanda;
import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.entities.Pronostico;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.DemandaPronosticadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
    public List<DemandaPronosticada> promedioPonderado(List<Demanda> demandasHistoricasSeleccionadas) {
        return List.of();
    }

    @Override
    public List<DemandaPronosticada> promedioMovil(List<Demanda> demandasHistoricasSeleccionadas, int n, int cantidadPeriodosAPredecir) {
        List<DemandaPronosticada> demandaPronosticadaList = new ArrayList<>();
        int periodoInicioPredecir = demandasHistoricasSeleccionadas.get(0).getNumPeriodo() + n;

        for (int i = 0; i < cantidadPeriodosAPredecir; i++) {
            double suma = 0;
            for (int j = 0; j < n; j++) {
                if (i + j < demandasHistoricasSeleccionadas.size()) {
                    suma += demandasHistoricasSeleccionadas.get(i + j).getCantTotalDemanda();
                }
            }
            double promedioMovil = suma / n;
            DemandaPronosticada dp = crearDemandaPronosticada(demandasHistoricasSeleccionadas, periodoInicioPredecir + i, promedioMovil);
            demandaPronosticadaList.add(dp);
        }
        return demandaPronosticadaList;
    }

    @Override
    public List<DemandaPronosticada> promedioPonderado(List<Demanda> demandasHistoricasSeleccionadas, int n, int cantidadPeriodosAPredecir) {
        List<DemandaPronosticada> demandaPronosticadaList = new ArrayList<>();
        double[] ponderaciones = calcularPonderaciones(n);

        for (int i = 0; i < cantidadPeriodosAPredecir; i++) {
            double sumaPonderada = 0;
            for (int j = 0; j < n; j++) {
                sumaPonderada += demandasHistoricasSeleccionadas.get(i + j).getCantTotalDemanda() * ponderaciones[j];
            }
            DemandaPronosticada dp = crearDemandaPronosticada(demandasHistoricasSeleccionadas, i + n, sumaPonderada);
            demandaPronosticadaList.add(dp);
        }
        return demandaPronosticadaList;
    }

    @Override
    public List<DemandaPronosticada> suavizacionExponencial(List<Demanda> demandasHistoricasSeleccionadas, double alpha, int mesDemandaAnterior, int cantidadRealDemandadaAnterior, int cantidadDemandadaPronosticadaAnterior) {
        List<DemandaPronosticada> demandasPronosticadas = new ArrayList<>();
        double prediccionSuavizacionExponencial = cantidadDemandadaPronosticadaAnterior + alpha * (cantidadRealDemandadaAnterior - cantidadDemandadaPronosticadaAnterior);

        DemandaPronosticada dp = crearDemandaPronosticada(demandasHistoricasSeleccionadas, mesDemandaAnterior + 1, prediccionSuavizacionExponencial);
        demandasPronosticadas.add(dp);
        return demandasPronosticadas;
    }

    @Override
    public List<DemandaPronosticada> pronosticoEstacional(List<Demanda> demandasHistoricas, int anioAPredecir, int anioInicio, int anioFin, int demandaEsperada) {
        List<DemandaPronosticada> demandasPronosticadas = new ArrayList<>();
        List<List<Demanda>> demandasPorAnio = new ArrayList<>();

        for (int anio = anioInicio; anio <= anioFin; anio++) {
            demandasPorAnio.add(seleccionarDemandaHistoricas(demandasHistoricas, anio));
        }

        double promedioTotal = calcularPromedioTotal(demandasPorAnio);
        double[] indicesEstacionalidad = calcularIndicesEstacionalidad(demandasPorAnio, promedioTotal);

        for (int mes = 0; mes < 12; mes++) {
            double cantidadPronosticada = demandaEsperada * indicesEstacionalidad[mes];
            DemandaPronosticada dp = new DemandaPronosticada();
            dp.setCantidadDemandadaPronostico(cantidadPronosticada);
            dp.setAnioDemandaPronosticada(anioAPredecir);
            dp.setNroPeriodoDemandaPronosticada(mes + 1);
            demandasPronosticadas.add(dp);
        }
        return demandasPronosticadas;
    }

    private double calcularPromedioTotal(List<List<Demanda>> demandasPorAnio) {
        int suma = 0;
        int contador = 0;

        for (List<Demanda> demandasAnio : demandasPorAnio) {
            for (Demanda demanda : demandasAnio) {
                suma += demanda.getCantTotalDemanda();
                contador++;
            }
        }
        return (double) suma / contador;
    }

    private double[] calcularIndicesEstacionalidad(List<List<Demanda>> demandasPorAnio, double promedioTotal) {
        int cantidadAnios = demandasPorAnio.size();
        double[][] indicesPorAnio = new double[12][cantidadAnios];
        double[] indicesEstacionalidad = new double[12];

        for (int anio = 0; anio < cantidadAnios; anio++) {
            for (int mes = 0; mes < 12; mes++) {
                indicesPorAnio[mes][anio] = demandasPorAnio.get(anio).get(mes).getCantTotalDemanda() / promedioTotal;
            }
        }

        for (int mes = 0; mes < 12; mes++) {
            double suma = 0;
            for (int anio = 0; anio < cantidadAnios; anio++) {
                suma += indicesPorAnio[mes][anio];
            }
            indicesEstacionalidad[mes] = suma / cantidadAnios;
        }
        return indicesEstacionalidad;
    }

    private DemandaPronosticada crearDemandaPronosticada(List<Demanda> demandasHistoricasSeleccionadas, int periodo, double cantidadPronosticada) {
        DemandaPronosticada dp = new DemandaPronosticada();
        dp.setNroPeriodoDemandaPronosticada(periodo);
        dp.setCantidadDemandadaPronostico(cantidadPronosticada);
        dp.setAnioDemandaPronosticada(demandasHistoricasSeleccionadas.get(0).getAnio());
        return dp;
    }

    private double[] calcularPonderaciones(int n) {
        double[] ponderaciones = new double[n];
        double sumaPonderaciones = 0;

        for (int i = 1; i <= n; i++) {
            sumaPonderaciones += i;
        }

        for (int i = 0; i < n; i++) {
            ponderaciones[i] = (double) (i + 1) / sumaPonderaciones;
        }
        return ponderaciones;
    }

    @Override
    public List<DemandaPronosticada> predecirDemanda(List<Demanda> demandas) {
        // Implementación del método de predicción
        return List.of();
    }

    @Override
    public DemandaPronosticada getPronostico(Long id) {
        Optional<DemandaPronosticada> optionalPronostico = demandaPronosticadaRepository.findById(id);
        return optionalPronostico.orElse(null);
    }

    @Override
    public DemandaPronosticada updatePronostico(Long id, DemandaPronosticada pronosticoActualizado) {
        if (demandaPronosticadaRepository.existsById(id)) {
            pronosticoActualizado.setId(id);
            return demandaPronosticadaRepository.save(pronosticoActualizado);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void deletePronostico(Long id) {
        demandaPronosticadaRepository.deleteById(id);
    }

    @Override
    public List<DemandaPronosticada> getAllPronosticos() {
        return demandaPronosticadaRepository.findAll();
    }
}
