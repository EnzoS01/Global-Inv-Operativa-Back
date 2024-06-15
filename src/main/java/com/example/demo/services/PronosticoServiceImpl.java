package com.example.demo.services;

import com.example.demo.entities.Demanda;
import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.entities.Pronostico;
import com.example.demo.repositories.BaseRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class PronosticoServiceImpl extends BaseServiceImpl<Pronostico, Long> implements PronosticoService {

    public PronosticoServiceImpl(BaseRepository<Pronostico, Long> baseRepository) {
        super(baseRepository);
        //TODO Auto-generated constructor stub
    }

    private List<DemandaPronosticada> demandaPronosticada;

    @Override
    public List<Demanda> seleccionarDemandaHistoricas(List<Demanda> listaDemandasHistoricas, int nroAnio) {
        return listaDemandasHistoricas.stream()
                .filter(demanda -> demanda.getAnio() == nroAnio)
                .collect(Collectors.toList());
    }

    @Override
    public List<DemandaPronosticada> promedioMovil(List<Demanda> demandasHistoricasSeleccionadas) {
        Scanner entrada = new Scanner(System.in);
        List<DemandaPronosticada> demandaPronosticada3 = new ArrayList<>();

        System.out.println("Cuántos periodos se van a utilizar por cálculo: ");
        int n = entrada.nextInt();

        System.out.println("Cuántos periodos futuros desea pronosticar?: ");
        int cantidadPeriodosAPredecir = entrada.nextInt();

        int periodoInicioPredecir = demandasHistoricasSeleccionadas.get(0).getNumPeriodo() + n;

        for (int i = 0; i < cantidadPeriodosAPredecir; i++) {
            DemandaPronosticada dp = new DemandaPronosticada();
            dp.setNroPeriodoDemandaPronosticada(periodoInicioPredecir + i);

            double suma = 0;
            int indiceInicio = i;
            int indiceFin = i + n - 1;

            for (int j = indiceInicio; j <= indiceFin; j++) {
                if (j < demandasHistoricasSeleccionadas.size()) {
                    suma += demandasHistoricasSeleccionadas.get(j).getCantTotalDemanda();
                } else {
                    System.out.println("Índice fuera de rango: " + j);
                }
            }

            double promedioMovil = suma / n;
            System.out.println(
                    "Promedio móvil calculado para el periodo " + (periodoInicioPredecir + i) + ": " + promedioMovil);
            dp.setCantidadDemandadaPronostico(promedioMovil);
            dp.setAnioDemandaPronosticada(demandasHistoricasSeleccionadas.get(0).getAnio());
            dp.setNroPeriodoDemandaPronosticada(periodoInicioPredecir + i + n);
            dp.setDemandaRealAsociada(demandasHistoricasSeleccionadas.get(periodoInicioPredecir + i + n - 1));
            demandaPronosticada3.add(dp);
        }
        return demandaPronosticada3;
    }

    @Override
    public List<DemandaPronosticada> promedioPonderado(List<Demanda> demandasHistoricasSeleccionadas) {
        Scanner entrada = new Scanner(System.in);
        List<DemandaPronosticada> demandaPronosticada = new ArrayList<>();

        System.out.println("Cuántos periodos históricos desea utilizar para el cálculo?: ");
        int n = entrada.nextInt();

        System.out.println("Cuántos periodos futuros desea pronosticar?: ");
        int cantidadPeriodosAPredecir = entrada.nextInt();

        double[] ponderaciones = calcularPonderaciones(n);

        if (demandasHistoricasSeleccionadas.size() < n) {
            System.out.println("No hay suficientes datos históricos para realizar el cálculo.");
            return demandaPronosticada;
        }

        int periodoInicioPredecir = demandasHistoricasSeleccionadas.get(0).getNumPeriodo() + 1 - n;

        for (int i = 0; i < cantidadPeriodosAPredecir; i++) {
            double sumaPonderada = 0;

            for (int j = 0; j < n; j++) {
                int indiceHistorico = i + j;
                sumaPonderada += demandasHistoricasSeleccionadas.get(indiceHistorico).getCantTotalDemanda()
                        * ponderaciones[j];
            }

            DemandaPronosticada dp = new DemandaPronosticada();
            dp.setNroPeriodoDemandaPronosticada(periodoInicioPredecir + i + n);
            dp.setAnioDemandaPronosticada(demandasHistoricasSeleccionadas.get(0).getAnio());
            dp.setCantidadDemandadaPronostico(sumaPonderada);
            dp.setDemandaRealAsociada(demandasHistoricasSeleccionadas.get(periodoInicioPredecir + i + n - 1));
            demandaPronosticada.add(dp);
        }

        return demandaPronosticada;
    }

    @Override
    public List<DemandaPronosticada> suavizacionExponencial(List<Demanda> demandasHistoricasSeleccionadas) {
        Scanner entrada = new Scanner(System.in);
        List<DemandaPronosticada> demandasPronosticadas = new ArrayList<>();

        System.out.println("\nMétodo de predicción Suavización exponencial");
        System.out.println("\nIngrese el valor del coeficiente alpha: ");
        double alpha = entrada.nextDouble();
        double prediccionSuavizacionExponencial = 0;
        int mesDemandaAnterior = 0;
        int cantidadRealDemandadaAnterior = 0;
        int cantidadDemandadaPronosticadaAnterior = 0;

        System.out.println("Opciones: ");
        System.out.println("1- Ingresar la cantidad demandada real del mes anterior al mes a predecir por teclado.");
        System.out.println("2- Ingresar la número de mes de la demanda anterior del mes anterior al mes a predecir");

        int seleccion = entrada.nextInt();

        if (seleccion == 1) {
            System.out
                    .println("\nIngrese el número de mes de la demanda real del mes anterior al que desea predecir: ");
            mesDemandaAnterior = entrada.nextInt();
            System.out.println(
                    "\nIngrese la cantidad demandada de la demanda real del mes anterior al que desea predecir: ");
            cantidadRealDemandadaAnterior = entrada.nextInt();
            System.out.println(
                    "\nIngrese la cantidad demandada pronosticada del mes anterior al que desea pronosticar (demanda pronosticada raíz): ");
            cantidadDemandadaPronosticadaAnterior = entrada.nextInt();
            prediccionSuavizacionExponencial = cantidadDemandadaPronosticadaAnterior
                    + alpha * (cantidadRealDemandadaAnterior - cantidadDemandadaPronosticadaAnterior);
        } else {
            System.out
                    .println("\nIngrese el número de mes de la demanda real del mes anterior al que desea predecir: ");
            mesDemandaAnterior = entrada.nextInt();

            Demanda demandaRealAnterior = demandasHistoricasSeleccionadas.get(mesDemandaAnterior - 1);
            cantidadRealDemandadaAnterior = demandaRealAnterior.getCantTotalDemanda();

            System.out.println(
                    "\nIngrese la cantidad demandada pronosticada del mes anterior al que desea pronosticar (demanda pronosticada raíz): ");
            cantidadDemandadaPronosticadaAnterior = entrada.nextInt();
            prediccionSuavizacionExponencial = cantidadDemandadaPronosticadaAnterior
                    + alpha * (cantidadRealDemandadaAnterior - cantidadDemandadaPronosticadaAnterior);
        }

        DemandaPronosticada dp = new DemandaPronosticada();
        dp.setAnioDemandaPronosticada(demandasHistoricasSeleccionadas.get(0).getAnio());
        dp.setCantidadDemandadaPronostico(prediccionSuavizacionExponencial);
        dp.setNroPeriodoDemandaPronosticada(mesDemandaAnterior + 1);
        dp.setDemandaRealAsociada(demandasHistoricasSeleccionadas.get(mesDemandaAnterior - 1));
        demandasPronosticadas.add(dp);

        System.out.println("Demanda pronosticada del mes anterior " + mesDemandaAnterior + " : "
                + cantidadDemandadaPronosticadaAnterior);
        System.out
                .println("Demanda real del mes anterior " + mesDemandaAnterior + " : " + cantidadRealDemandadaAnterior);
        System.out.println("Demanda pronosticada para el mes " + dp.getNroPeriodoDemandaPronosticada() + " : "
                + prediccionSuavizacionExponencial);

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
