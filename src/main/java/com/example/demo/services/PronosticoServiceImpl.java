package com.example.demo.services;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.Demanda;
import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.entities.Pronostico;
import com.example.demo.repositories.*;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PronosticoServiceImpl extends BaseServiceImpl<Pronostico, Long> implements PronosticoService {

    @Autowired
    private DemandaPronosticadaRepository demandaPronosticadaRepository;
    @Autowired
    private PronosticoRepository pronosticoRepository;
    @Autowired
    private ArticuloRepository articuloRepository;
    @Autowired
    private DemandaRepository demandaRepository;

    public PronosticoServiceImpl(BaseRepository<Pronostico, Long> baseRepository,
            DemandaPronosticadaRepository demandaPronosticadaRepository) {
        super(baseRepository);
        this.demandaPronosticadaRepository = demandaPronosticadaRepository;
    }

    @Override
    public Pronostico asignarArticulo(Long pronosticoId,Long articuloId){
        Pronostico pron = pronosticoRepository.findById(pronosticoId)
                .orElseThrow(() -> new RuntimeException("pronostico no encontrado"));
        Articulo articulo = articuloRepository.findById(articuloId)
                .orElseThrow(() -> new RuntimeException("articulo no encontrado"));

        pron.setArticulo(articulo);
        pronosticoRepository.save(pron);
        return pron;
    }

    @Override
    public double[] calcularPonderaciones(int n) {
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
    public int periodoActual(Pronostico pron, int anioActual){
        List<Demanda> demandas = demandaRepository.findByArticuloAndAnio(pron.getArticulo(), anioActual);
        return demandas.stream()
                .max(Comparator.comparing(Demanda::getNumPeriodo))
                .map(demanda -> {
                    int nextPeriod = demanda.getNumPeriodo() + 1;
                    return nextPeriod > 12 ? 1 : nextPeriod; //si el NumPeriodo de la demanda es 12, devuelve 1
                })
                .orElse(1); // Si no hay demandas, empezamos desde el periodo 1
    }

    @Override
    public DemandaPronosticada crearDPronosticada(int anioActual, int periodoActual, double pronostico,Articulo articulo){
        DemandaPronosticada demandaPronosticada= new DemandaPronosticada();
        List<Demanda> demandas = demandaRepository.findByArticuloAndAnio(articulo, anioActual);

        // Determina si el periodo actual es 1 debido a un nuevo año
        boolean esNuevoAnio = !demandas.isEmpty() && periodoActual == 1 && demandas.stream()
                .max(Comparator.comparing(Demanda::getNumPeriodo))
                .map(demanda -> demanda.getNumPeriodo() == 12)
                .orElse(false);

        if (esNuevoAnio) {
            demandaPronosticada.setAnioDemandaPronosticada(anioActual + 1);
        } else {
            demandaPronosticada.setAnioDemandaPronosticada(anioActual);
        }

        demandaPronosticada.setCantidadDemandadaPronostico(pronostico);
        demandaPronosticada.setNroPeriodoDemandaPronosticada(periodoActual);
        return demandaPronosticada;
    }

    @Override
    public Pronostico promedioPonderado(Long pronosticoId,int anio){
        Pronostico pron = pronosticoRepository.findById(pronosticoId)
                .orElseThrow(() -> new RuntimeException("pronostico no encontrado"));
        int cantidadPeriodos=pron.getCantidadPeriodosHistoricos();
        double[] ponderaciones= calcularPonderaciones(cantidadPeriodos);
        int periodoActual= periodoActual(pron,anio);

        //Ordena la lista demandas de manera descendente en la posicion 0 está la demanda con el numero de periodo más alto
        List<Demanda> demandas= demandaRepository.findByArticuloAndAnio(pron.getArticulo(),anio);
        demandas.sort(Comparator.comparingInt(Demanda::getNumPeriodo).reversed());

        double promedio = 0;
        for (int i=0; i<cantidadPeriodos;i++){
            promedio+=demandas.get(i).getCantTotalDemanda()*ponderaciones[cantidadPeriodos-(i+1)];
        }

        DemandaPronosticada demandaPronosticada= crearDPronosticada(anio,periodoActual,promedio,pron.getArticulo());
        demandaPronosticadaRepository.save(demandaPronosticada);
        pron.setDemandaPronosticada(demandaPronosticada);
        pronosticoRepository.save(pron);
        return pron;
    }

    @Override
    public Pronostico pmSuavizado(Long pronosticoId, double predecidaRaiz, double valorCoeficiente, int anio){
        Pronostico pron = pronosticoRepository.findById(pronosticoId)
                .orElseThrow(() -> new RuntimeException("pronostico no encontrado"));
        int periodoActual= periodoActual(pron,anio);

        //Buscar la demanda asociada al mismo articulo que el pronóstico con el año correspondiente y el periodo anterior
        Demanda demandaReal= demandaRepository.findByArticuloAndAnioAndNumPeriodo(pron.getArticulo(),anio,periodoActual-1);

        //Se hace la cuenta
        double pronostico= predecidaRaiz + valorCoeficiente*(demandaReal.getCantTotalDemanda()-predecidaRaiz);

        DemandaPronosticada demandaPronosticada= crearDPronosticada(anio,periodoActual,pronostico,pron.getArticulo());
        demandaPronosticadaRepository.save(demandaPronosticada);
        pron.setDemandaPronosticada(demandaPronosticada);
        pronosticoRepository.save(pron);

        return pron;
    }

    @Override
    public Pronostico regresionLineal(Long pronosticoId, int anio){
        Pronostico pron = pronosticoRepository.findById(pronosticoId)
                .orElseThrow(() -> new RuntimeException("pronostico no encontrado"));
        int periodoActual= periodoActual(pron,anio);


        //Ordena la lista demandas de manera ascendente en la posicion 0 está la demanda con el numero de periodo más bajo
        List<Demanda> demandasReales= demandaRepository.findByArticuloAndAnio(pron.getArticulo(),anio);
        demandasReales.sort(Comparator.comparingInt(Demanda::getNumPeriodo));

        double sumatoriaX=0;
        double sumatoriaY=0;
        double sumatoriaXCuadrado=0;
        double sumatoriaMultiplicacionXY=0;

        for (int i=0; i<periodoActual-1; i++){
            System.out.println("NumeroPeriodo: "+ demandasReales.get(i).getNumPeriodo());
            sumatoriaX+=demandasReales.get(i).getNumPeriodo();
            System.out.println("sumatoriaX: "+sumatoriaX);
            sumatoriaY+=demandasReales.get(i).getCantTotalDemanda();
            System.out.println("sumatoriaY: "+sumatoriaY);

            sumatoriaXCuadrado+= demandasReales.get(i).getNumPeriodo()*demandasReales.get(i).getNumPeriodo();
            System.out.println("SumatoriaXcuadrado: "+sumatoriaXCuadrado);
            sumatoriaMultiplicacionXY+=demandasReales.get(i).getNumPeriodo()*demandasReales.get(i).getCantTotalDemanda();
            System.out.println("sumatoriaMultiplicacionXY: "+sumatoriaMultiplicacionXY);
        }
        double promedioX= sumatoriaX/(periodoActual-1);
        double promedioY= sumatoriaY/(periodoActual-1);

        System.out.println("promedioX " + promedioX);
        System.out.println("PromedioY " +promedioY);

        double parteArribaB= sumatoriaMultiplicacionXY - (periodoActual-1)*(promedioX*promedioY);
        double parteAbajoB=  sumatoriaXCuadrado - (periodoActual-1)*(promedioX*promedioX);

        double b= parteArribaB/parteAbajoB;
        System.out.println("b "+ b);
        double a= promedioY -(b*promedioX);
        System.out.println("a " + a);

        double pronostico= a + b*periodoActual;

        DemandaPronosticada demandaPronosticada= crearDPronosticada(anio,periodoActual,pronostico,pron.getArticulo());
        demandaPronosticadaRepository.save(demandaPronosticada);
        pron.setDemandaPronosticada(demandaPronosticada);
        pronosticoRepository.save(pron);

        return pron;
    }
    @Override
    public Pronostico pronosticoEstacionalidad(Long pronosticoId, int anioAPredecir, double demandaEsperada){
        Pronostico pron = pronosticoRepository.findById(pronosticoId)
                .orElseThrow(() -> new RuntimeException("pronostico no encontrado"));
        int cantidadAniosUsar= pron.getCantidadPeriodosHistoricos();
        Long artId=pron.getArticulo().getId();
        List<List<Demanda>> listaDemandasAnio= new ArrayList<>();
        int periodoAPredecir= periodoActual(pron,anioAPredecir);

        int cantidadDemandas;
        double demandaPromedioAnual;
        double indiceEstacional;
        double contadorPromedioMensual=0;
        double demandaPromedioMensual;
        int cantidadPeriodosContador=0;
        double contadorPromedioAnual=0;
        double pronostico;

        for(int i=1; i<cantidadAniosUsar+1; i++){
            listaDemandasAnio.add(demandaRepository.findByArticuloAndAnio(pron.getArticulo(),anioAPredecir-i)); //van de forma descendente por ejemplo: en la posicion 0 se guardan las demandas de 2023, en la posicion 1 las demandas de 2022,etc
            listaDemandasAnio.get(i-1).sort(Comparator.comparingInt(Demanda::getNumPeriodo));

            for (Demanda demanda: listaDemandasAnio.get(i-1)){
                contadorPromedioMensual+= demanda.getCantTotalDemanda();
            }
            cantidadPeriodosContador+= listaDemandasAnio.get(i-1).size();

            contadorPromedioAnual+=listaDemandasAnio.get(i-1).get(periodoAPredecir-1).getCantTotalDemanda();

        }

        demandaPromedioMensual=contadorPromedioMensual/cantidadPeriodosContador;
        demandaPromedioAnual= contadorPromedioAnual/cantidadAniosUsar;
        indiceEstacional=demandaPromedioAnual/demandaPromedioMensual;
        pronostico= (demandaEsperada/12)* indiceEstacional;

        DemandaPronosticada demandaPronosticada= crearDPronosticada(anioAPredecir,periodoAPredecir,pronostico,pron.getArticulo());
        demandaPronosticadaRepository.save(demandaPronosticada);
        pron.setDemandaPronosticada(demandaPronosticada);
        pronosticoRepository.save(pron);

        return pron;
    }



    /*
    @Override
    public List<DemandaPronosticada> predecirDemanda(List<Demanda> demandas) {
        // Implementación del método de predicción
        return List.of();
    }

   /*
    // Código que va en archivo que contiene main()

    // package com.mycompany.sistemainventariosdemanda;

    // import java.util.ArrayList;
    // import java.util.List;

    // public class SistemaInventariosDemanda {

    // public static void main(String[] args) {

    // Articulo art = new Articulo("Producto A");

    // Pronostico pronostico1 = new Pronostico("PronosticoPromedioMovil", art);

    // System.out.println(pronostico1.getNumPronostico());

    // Pronostico pronostico2 = new Pronostico("PronosticoB", art);

    // System.out.println(pronostico2.getNumPronostico());

    // List<Demanda> listaDemandas = new ArrayList<>();

    // // Demanda para el año 2019
    // listaDemandas.add(new Demanda(1, 2019, 180, art));
    // listaDemandas.add(new Demanda(2, 2019, 168, art));
    // listaDemandas.add(new Demanda(3, 2019, 159, art));
    // listaDemandas.add(new Demanda(4, 2019, 175, art));
    // listaDemandas.add(new Demanda(5, 2019, 190, art));
    // listaDemandas.add(new Demanda(6, 2019, 205, art));
    // listaDemandas.add(new Demanda(7, 2019, 180, art));
    // listaDemandas.add(new Demanda(8, 2019, 182, art));

    // // Demanda para el año 2020
    // listaDemandas.add(new Demanda(1, 2020, 10, art));
    // listaDemandas.add(new Demanda(2, 2020, 12, art));
    // listaDemandas.add(new Demanda(3, 2020, 13, art));
    // listaDemandas.add(new Demanda(4, 2020, 16, art));
    // listaDemandas.add(new Demanda(5, 2020, 19, art));
    // listaDemandas.add(new Demanda(6, 2020, 23, art));
    // listaDemandas.add(new Demanda(7, 2020, 26, art));
    // listaDemandas.add(new Demanda(8, 2020, 30, art));
    // listaDemandas.add(new Demanda(9, 2020, 28, art));
    // listaDemandas.add(new Demanda(10, 2020, 18, art));
    // listaDemandas.add(new Demanda(11, 2020, 16, art));
    // listaDemandas.add(new Demanda(12, 2020, 14, art));

    // //Para demanda estacional
    // // Demanda para el año 2022
    // listaDemandas.add(new Demanda(1, 2021, 80, art));
    // listaDemandas.add(new Demanda(2, 2021, 70, art));
    // listaDemandas.add(new Demanda(3, 2021, 80, art));
    // listaDemandas.add(new Demanda(4, 2021, 90, art));
    // listaDemandas.add(new Demanda(5, 2021, 113, art));
    // listaDemandas.add(new Demanda(6, 2021, 110, art));
    // listaDemandas.add(new Demanda(7, 2021, 100, art));
    // listaDemandas.add(new Demanda(8, 2021, 88, art));
    // listaDemandas.add(new Demanda(9, 2021, 85, art));
    // listaDemandas.add(new Demanda(10, 2021, 77, art));
    // listaDemandas.add(new Demanda(11, 2021, 75, art));
    // listaDemandas.add(new Demanda(12, 2021, 82, art));

    // // Demanda para el año 2022
    // listaDemandas.add(new Demanda(1, 2022, 85, art));
    // listaDemandas.add(new Demanda(2, 2022, 85, art));
    // listaDemandas.add(new Demanda(3, 2022, 93, art));
    // listaDemandas.add(new Demanda(4, 2022, 95, art));
    // listaDemandas.add(new Demanda(5, 2022, 125, art));
    // listaDemandas.add(new Demanda(6, 2022, 115, art));
    // listaDemandas.add(new Demanda(7, 2022, 102, art));
    // listaDemandas.add(new Demanda(8, 2022, 102, art));
    // listaDemandas.add(new Demanda(9, 2022, 90, art));
    // listaDemandas.add(new Demanda(10, 2022, 78, art));
    // listaDemandas.add(new Demanda(11, 2022, 82, art));
    // listaDemandas.add(new Demanda(12, 2022, 78, art));

    // // Demanda para el año 2023

    // listaDemandas.add(new Demanda(1, 2023, 105, art));
    // listaDemandas.add(new Demanda(2, 2023, 85, art));
    // listaDemandas.add(new Demanda(3, 2023, 82, art));
    // listaDemandas.add(new Demanda(4, 2023, 115, art));
    // listaDemandas.add(new Demanda(5, 2023, 131, art));
    // listaDemandas.add(new Demanda(6, 2023, 120, art));
    // listaDemandas.add(new Demanda(7, 2023, 113, art));
    // listaDemandas.add(new Demanda(8, 2023, 110, art));
    // listaDemandas.add(new Demanda(9, 2023, 95, art));
    // listaDemandas.add(new Demanda(10, 2023, 85, art));
    // listaDemandas.add(new Demanda(11, 2023, 83, art));
    // listaDemandas.add(new Demanda(12, 2023, 80, art));

    // List<DemandaPronosticada> demandasPronosticadas= new ArrayList<>();

    // demandasPronosticadas = pronostico1.predecirDemanda(listaDemandas);

    // // for (DemandaPronosticada dp : demandasPronosticadas){
    // // System.out.println("\nMes "+ dp.getNroPeriodoDemandaPronosticada() + " :
    // ");
    // // System.out.println("\nDemanda pronosticada: " +
    // dp.getCantidadDemandadaPronostico());
    // // }

    // }
    // }

    // Fin

    // Código JAVA

    // package com.mycompany.sistemainventariosdemanda;

    // import java.time.Year;
    // import java.util.*;
    // import java.util.stream.Collectors;

    // public class Pronostico {
    // private int numPronostico;
    // private String nombrePronostico;
    // private double errorAceptable;
    // private int cantidadPeriodosAPredecir;
    // private int cantidadPeriodosHistoricos;
    // private static int contador = 0;
    // private List<DemandaPronosticada> demandaPronosticada;
    // private Articulo articulo;

    // public Pronostico(String nombrePronostico, Articulo art) {
    // contador++;
    // this.numPronostico = contador;
    // this.nombrePronostico = nombrePronostico;
    // this.articulo = art;
    // }

    // private int calcularCantidadFaltante(Articulo articulo, DemandaPronosticada
    // dp) {
    // int stockActual = articulo.getCantActual();
    // int stockSeguridad = articulo.getStockSeguridad();
    // int puntoPedido = articulo.getPuntoPedido();
    // int loteOptimo = articulo.getLoteOptimo();
    // double cantidadDemandadaPronosticada = dp.getCantidadDemandadaPronostico();
    // int periodo = dp.getNroPeriodoDemandaPronosticada();

    // Lógica para calcular la cantidad faltante basada en el pronóstico de demanda
    // int cantidadNecesaria = 0;

    // return cantidadNecesaria > 0 ? cantidadNecesaria : 0;
    // }

    // /**
    // * Este método calcula la Desviación Absoluta Media (MAD) para una lista de
    // demandas pronosticadas.
    // * La MAD es una medida de la precisión de las predicciones. Calcula el
    // promedio de las diferencias absolutas
    // * entre los valores de demanda pronosticados y los valores de demanda reales.
    // Cuanto menor sea la MAD, más precisas son las predicciones.
    // *
    // * @param demandasPronosticadas Una lista de demandas pronosticadas.
    // * @return La Desviación Absoluta Media de las demandas pronosticadas.
    // */
    // public double obtenerDesviacionAbsolutaMedia(List<DemandaPronosticada>
    // demandasPronosticadas) {
    // double suma = 0;
    // int periodosValidos = 0;
    // for (DemandaPronosticada dp : demandasPronosticadas) {
    // if (dp.getDemandaRealAsociada() != null) {
    // suma += Math.abs(
    // dp.getDemandaRealAsociada().getCantidadTotalDemandada() -
    // dp.getCantidadDemandadaPronostico());
    // periodosValidos++;
    // }
    // }
    // return periodosValidos > 0 ? suma / periodosValidos : 0;
    // }

    // /**
    // * Este método calcula el Error Medio Absoluto Porcentual (MAPE) para una
    // lista de demandas pronosticadas.
    // * El MAPE es una medida de la precisión de las predicciones. Calcula el
    // promedio de las diferencias absolutas
    // * entre los valores de demanda pronosticados y los valores de demanda reales,
    // dividido por el valor de demanda real.
    // * Este resultado se expresa en términos porcentuales. Cuanto menor sea el
    // MAPE, más precisas son las predicciones.
    // *
    // * @param demandasPronosticadas Una lista de demandas pronosticadas.
    // * @return El Error Medio Absoluto Porcentual de las demandas pronosticadas.
    // */
    // public double obtenerErrorMedioAbsolutoPorcentual(List<DemandaPronosticada>
    // demandasPronosticadas) {
    // double suma = 0;
    // int periodosValidos = 0;
    // for (DemandaPronosticada dp : demandasPronosticadas) {
    // if (dp.getDemandaRealAsociada() == null ||
    // dp.getDemandaRealAsociada().getCantidadTotalDemandada() == 0) {
    // continue; // Salta este período si la demanda real asociada es null o la
    // cantidad total
    // demandada es 0
    // }
    // suma += 100
    // * Math.abs(dp.getCantidadDemandadaPronostico()
    // - dp.getDemandaRealAsociada().getCantidadTotalDemandada())
    // / dp.getDemandaRealAsociada().getCantidadTotalDemandada();
    // periodosValidos++;
    // }
    // return periodosValidos > 0 ? suma / periodosValidos : 0; // Evita la división
    // por cero si no hay períodos
    // válidos
    // }

    // /**
    // * Este método calcula el Error Cuadrático Medio (MSE) para una lista de
    // demandas pronosticadas.
    // * El MSE es una medida de la precisión de las predicciones. Calcula el
    // promedio de las diferencias al cuadrado
    // * entre los valores de demanda pronosticados y los valores de demanda reales.
    // Cuanto menor sea el MSE, más precisas son las predicciones.
    // *
    // * @param demandasPronosticadas Una lista de demandas pronosticadas.
    // * @return El Error Cuadrático Medio de las demandas pronosticadas.
    // */
    // public double obtenerErrorCuadraticoMedio(List<DemandaPronosticada>
    // demandasPronosticadas) {
    // double suma = 0;
    // int periodosValidos = 0;
    // for (DemandaPronosticada dp : demandasPronosticadas) {
    // if (dp.getDemandaRealAsociada() == null) {
    // continue; // Salta este período si la demanda real asociada es null
    // }
    // suma += Math.pow(
    // dp.getCantidadDemandadaPronostico() -
    // dp.getDemandaRealAsociada().getCantidadTotalDemandada(), 2);
    // periodosValidos++;
    // }
    // return periodosValidos > 0 ? suma / periodosValidos : 0; // Evita la división
    // por cero si no hay períodos
    // válidos
    // }

    // /**
    // * Este método selecciona las demandas históricas que corresponden a un año
    // específico.
    // * Utiliza el método 'stream' para crear un flujo de datos a partir de la
    // lista de demandas históricas,
    // * luego aplica un filtro para seleccionar solo las demandas que corresponden
    // al año proporcionado.
    // *
    // * @param listaDemandasHistoricas Una lista de demandas históricas.
    // * @param nroAnio El año para el cual se deben seleccionar las demandas.
    // * @return Una lista de demandas que corresponden al año proporcionado.
    // */
    // public List<Demanda> seleccionarDemandaHistoricas(List<Demanda>
    // listaDemandasHistoricas, int nroAnio) {
    // Verificar que la lista no sea null
    // if (listaDemandasHistoricas == null) {
    // throw new IllegalArgumentException("La lista de demandas históricas no puede
    // ser null");
    // }

    // Validar que el número de año sea razonable (ejemplo: mayor que 1900 y menor o
    // igual al año actual)
    // int anioActual = Year.now().getValue();
    // if (nroAnio < 1900 || nroAnio > anioActual) {
    // throw new IllegalArgumentException("El número de año debe estar entre 1900 y
    // " + anioActual);
    // }

    // Filtrar y retornar la lista de demandas para el año especificado
    // return listaDemandasHistoricas.stream()
    // .filter(demanda -> demanda.getAnioDemanda() == nroAnio)
    // .collect(Collectors.toList());
    // }

    // /**
    // * Este método calcula la demanda pronosticada utilizando el método de
    // Promedio Móvil.
    // * El método de Promedio Móvil es una técnica de pronóstico que utiliza el
    // promedio de los 'n' periodos anteriores
    // * para predecir la demanda del próximo periodo.
    // *
    // * @param demandasHistoricas Una lista de demandas históricas seleccionadas.
    // * @return Una lista de demandas pronosticadas.
    // */
    // public List<DemandaPronosticada> promedioMovil(List<Demanda>
    // demandasHistoricas){
    // Scanner entrada = new Scanner(System.in);
    // List<DemandaPronosticada> listaDemandaPronosticada = new ArrayList<>();

    // int nroAnio = 0;

    // System.out.println("Método de cálculo de promedio móvil.");

    // System.out.println("Ingrese el año de demanda histórica: ");
    // nroAnio = entrada.nextInt();

    // if (nroAnio <= 0) {
    // System.out.println("El año debe ser mayor que cero.");
    // entrada.close();
    // return listaDemandaPronosticada;
    // }

    // List<Demanda> listaDemandasSeleccionadas =
    // seleccionarDemandaHistoricas(demandasHistoricas, nroAnio);

    // System.out.println("¿Cuántos períodos desea utilizar por cálculo?: ");
    // int cantidadPeriodosPorCalculo = entrada.nextInt();

    // if (cantidadPeriodosPorCalculo <= 2 & cantidadPeriodosPorCalculo>12) {
    // System.out.println("El número de períodos debe ser mayor o igual que dos y
    // menor o igual que doce.");
    // entrada.close();
    // return listaDemandaPronosticada;
    // }

    // this.cantidadPeriodosAPredecir = 0;
    // do {
    // System.out.println("Cuántos periodos desea calcular el promedio móvil?: ");
    // while (!entrada.hasNextInt()) {
    // System.out.println("Eso no es un número válido. Inténtalo de nuevo.");
    // entrada.next(); // descarta la entrada incorrecta
    // }
    // cantidadPeriodosAPredecir = entrada.nextInt();
    // if (cantidadPeriodosAPredecir <= 0) {
    // System.out.println("El número debe ser mayor que cero. Inténtalo de nuevo.");
    // } else if (cantidadPeriodosAPredecir > listaDemandasSeleccionadas.size() + 3)
    // {
    // System.out.println("El número no puede exceder la cantidad de demandas
    // históricas más 3. Inténtalo de nuevo.");
    // cantidadPeriodosAPredecir = 0; // reset the value so the loop continues
    // }
    // } while (cantidadPeriodosAPredecir <= 0);

    // if (listaDemandasSeleccionadas.size() == 12) {
    // this.cantidadPeriodosAPredecir = listaDemandasSeleccionadas.size() + 1 -
    // cantidadPeriodosPorCalculo; // Predecir el período 13
    // } else {
    // this.cantidadPeriodosAPredecir = listaDemandasSeleccionadas.size() -
    // cantidadPeriodosPorCalculo;
    // }

    // if (cantidadPeriodosAPredecir <= 0) {
    // System.out.println("La cantidad de períodos a predecir debe ser mayor que
    // cero.");
    // entrada.close();
    // return listaDemandaPronosticada;
    // }

    // Verificar si tenemos suficientes datos históricos para el cálculo
    // if (listaDemandasSeleccionadas.size() < 2 ||
    // listaDemandasSeleccionadas.size() < cantidadPeriodosPorCalculo) {
    // System.out.println("No hay suficientes datos históricos para realizar el
    // cálculo.");
    // entrada.close();
    // return listaDemandaPronosticada;
    // }

    // int periodoInicioPredecir =
    // listaDemandasSeleccionadas.getFirst().getNroPeriodoDemanda() +
    // cantidadPeriodosPorCalculo;

    // Repetimos por cada periodo a predecir.
    // for (int i = 0; i < cantidadPeriodosAPredecir; i++) {
    // DemandaPronosticada dp = new DemandaPronosticada();
    // dp.setNroPeriodoDemandaPronosticada(periodoInicioPredecir + i);

    // double suma = 0;
    // int indiceInicio = i;
    // int indiceFin = i + cantidadPeriodosPorCalculo - 1;
    // Repetimos por cada instancia por calculo ingresada
    // cantidadPeriodosPorCalculo.
    // for (int j = indiceInicio; j <= indiceFin; j++) {
    // if (j < listaDemandasSeleccionadas.size()) {
    // suma += listaDemandasSeleccionadas.get(j).getCantidadTotalDemandada();
    // System.out.println("Sumando demanda histórica del periodo " + (j+1) + ": " +
    // listaDemandasSeleccionadas.get(j).getCantidadTotalDemandada());
    // } else if (j - listaDemandasSeleccionadas.size() <
    // listaDemandaPronosticada.size()) {
    // Si no hay suficientes demandas históricas, usamos las demandas pronosticadas
    // anteriores
    // int indicePronosticada = listaDemandaPronosticada.size() - 1; // Accedemos al
    // último elemento de listaDemandaPronosticada
    // suma +=
    // listaDemandaPronosticada.get(indicePronosticada).getCantidadDemandadaPronostico();
    // System.out.println("Sumando demanda pronosticada del periodo " + (j + 1) + ":
    // " +
    // listaDemandaPronosticada.get(indicePronosticada).getCantidadDemandadaPronostico());
    // } else {
    // System.out.println("Índice fuera de rango: " + j);
    // }
    // }

    // double promedioMovil = suma / cantidadPeriodosPorCalculo;
    // dp.setCantidadDemandadaPronostico(promedioMovil);

    // Si tenemos 12 períodos históricos, incrementamos el año para la predicción
    // if (listaDemandasSeleccionadas.size() == 12 && i == cantidadPeriodosAPredecir
    // - 1) {
    // dp.setAnioDemandaPronosticada(listaDemandasSeleccionadas.getFirst().getAnioDemanda()
    // + 1);
    // } else {
    // dp.setAnioDemandaPronosticada(listaDemandasSeleccionadas.getFirst().getAnioDemanda());
    // }

    // if (periodoInicioPredecir + i < listaDemandasSeleccionadas.size()) {
    // dp.setDemandaRealAsociada(listaDemandasSeleccionadas.get(periodoInicioPredecir
    // + i-1));
    // } else {
    // dp.setDemandaRealAsociada(null); // No hay demanda real asociada para el
    // período futuro
    // }

    // listaDemandaPronosticada.add(dp);

    // System.out.println("Promedio móvil calculado para el periodo " +
    // dp.getNroPeriodoDemandaPronosticada()+ ": " +
    // dp.getCantidadDemandadaPronostico());
    // }

    // Cálculo de la MAD
    // Llamada al método obtenerDesviacionAbsolutaMedia
    // double mad = obtenerDesviacionAbsolutaMedia(listaDemandaPronosticada);

    // Mostrar el valor de la MAD
    // System.out.println("Desviación Absoluta Media (MAD): " + mad);

    // Cálculo de la MSE
    // Llamada al método
    // double mse = obtenerErrorCuadraticoMedio(listaDemandaPronosticada);

    // Mostrar el valor
    // System.out.println("Error cuadrático medio: " + mse);

    // Cálculo de la MAPE
    // Llamada al método
    // double mape = obtenerErrorMedioAbsolutoPorcentual(listaDemandaPronosticada);

    // Mostrar el valor1
    // System.out.println("Error Porcentual Absoluto Medio: " + mape);

    // entrada.close();
    // return listaDemandaPronosticada;
    // }

    // /**
    // * Este método calcula la demanda pronosticada utilizando el método de
    // Promedio Ponderado.
    // * El método de Promedio Ponderado es una técnica de pronóstico que utiliza el
    // promedio ponderado de los 'n' periodos anteriores
    // * para predecir la demanda del próximo periodo. Las ponderaciones se calculan
    // de tal manera que los periodos más recientes tienen
    // * un mayor peso en el cálculo del promedio.
    // *
    // * @param demandasHistoricas Una lista de demandas históricas seleccionadas.
    // * @return Una lista de demandas pronosticadas.
    // */
    // public List<DemandaPronosticada> promedioPonderado(List<Demanda>
    // demandasHistoricas) {
    // Scanner entrada = new Scanner(System.in);
    // List<DemandaPronosticada> listaDemandaPronosticada = new ArrayList<>();

    // int nroAnio = 0;

    // System.out.println("Método de cálculo de promedio ponderado.");

    // System.out.println("Ingrese el año de demanda histórica: ");
    // nroAnio = entrada.nextInt();

    // if (nroAnio <= 0) {
    // System.out.println("El año debe ser mayor que cero.");
    // entrada.close();
    // return listaDemandaPronosticada;
    // }

    // List<Demanda> listaDemandasSeleccionadas =
    // seleccionarDemandaHistoricas(demandasHistoricas, nroAnio);

    // System.out.println("¿Cuántos períodos desea utilizar por cálculo?: ");
    // int cantidadPeriodosPorCalculo = entrada.nextInt();

    // if (cantidadPeriodosPorCalculo < 2 || cantidadPeriodosPorCalculo > 12) {
    // System.out.println("El número de períodos debe ser mayor o igual que dos y
    // menor o igual que doce.");
    // entrada.close();
    // return listaDemandaPronosticada;
    // }

    // int cantidadPeriodosAPredecir = 0;
    // do {
    // System.out.println("¿Cuántos periodos futuros desea predecir?: ");
    // while (!entrada.hasNextInt()) {
    // System.out.println("Eso no es un número válido. Inténtalo de nuevo.");
    // entrada.next(); // descarta la entrada incorrecta
    // }
    // cantidadPeriodosAPredecir = entrada.nextInt();
    // if (cantidadPeriodosAPredecir <= 0) {
    // System.out.println("El número debe ser mayor que cero. Inténtalo de nuevo.");
    // } else if (cantidadPeriodosAPredecir > listaDemandasSeleccionadas.size() + 3)
    // {
    // System.out.println(
    // "El número no puede exceder la cantidad de demandas históricas más 3.
    // Inténtalo de nuevo.");
    // cantidadPeriodosAPredecir = 0; // reinicia el valor para que el bucle
    // continúe
    // }
    // } while (cantidadPeriodosAPredecir <= 0);

    // Calcular las ponderaciones
    // double[] ponderaciones = calcularPonderaciones(cantidadPeriodosPorCalculo);

    // Verificar si tenemos suficientes datos históricos para el cálculo
    // if (listaDemandasSeleccionadas.size() < cantidadPeriodosPorCalculo) {
    // System.out.println("No hay suficientes datos históricos para realizar el
    // cálculo.");
    // entrada.close();
    // return listaDemandaPronosticada;
    // }

    // int periodoInicioPredecir =
    // listaDemandasSeleccionadas.get(0).getNroPeriodoDemanda()
    // + cantidadPeriodosPorCalculo;

    // Calcular la demanda pronosticada aplicando las ponderaciones sobre las
    // demandas históricas seleccionadas
    // for (int i = 0; i < cantidadPeriodosAPredecir; i++) {
    // double sumaPonderada = 0;
    // int indiceInicio = i;
    // int indiceFin = i + cantidadPeriodosPorCalculo - 1;

    // for (int j = indiceInicio; j <= indiceFin; j++) {
    // if (j < listaDemandasSeleccionadas.size()) {
    // sumaPonderada +=
    // listaDemandasSeleccionadas.get(j).getCantidadTotalDemandada()
    // * ponderaciones[j - indiceInicio];
    // System.out.println("Sumando demanda histórica del periodo " + (j + 1) + "
    // ponderada: "
    // + (listaDemandasSeleccionadas.get(j).getCantidadTotalDemandada()
    // * ponderaciones[j - indiceInicio]));
    // } else if (j - listaDemandasSeleccionadas.size() <
    // listaDemandaPronosticada.size()) {
    // int indicePronosticada = listaDemandaPronosticada.size() - 1;
    // sumaPonderada +=
    // listaDemandaPronosticada.get(indicePronosticada).getCantidadDemandadaPronostico()
    // * ponderaciones[j - indiceInicio];
    // System.out.println("Sumando demanda pronosticada del periodo " + (j + 1) + "
    // ponderada: "
    // +
    // (listaDemandaPronosticada.get(indicePronosticada).getCantidadDemandadaPronostico()
    // * ponderaciones[j - indiceInicio]));
    // } else {
    // System.out.println("Índice fuera de rango: " + j);
    // }
    // }

    // DemandaPronosticada dp = new DemandaPronosticada();
    // dp.setNroPeriodoDemandaPronosticada(periodoInicioPredecir + i);
    // dp.setCantidadDemandadaPronostico(sumaPonderada);

    // if (listaDemandasSeleccionadas.size() == 12 && i == cantidadPeriodosAPredecir
    // - 1) {
    // dp.setAnioDemandaPronosticada(listaDemandasSeleccionadas.get(0).getAnioDemanda()
    // + 1);
    // } else {
    // dp.setAnioDemandaPronosticada(listaDemandasSeleccionadas.get(0).getAnioDemanda());
    // }

    // if (periodoInicioPredecir + i < listaDemandasSeleccionadas.size()+1) {
    // dp.setDemandaRealAsociada(listaDemandasSeleccionadas.get(periodoInicioPredecir
    // + i - 1));
    // } else {
    // dp.setDemandaRealAsociada(null);
    // }

    // listaDemandaPronosticada.add(dp);
    // System.out.println("Promedio ponderado calculado para el periodo " +
    // dp.getNroPeriodoDemandaPronosticada()
    // + ": " + dp.getCantidadDemandadaPronostico());
    // }

    // Cálculo de la MAD
    // Llamada al método obtenerDesviacionAbsolutaMedia
    // double mad = obtenerDesviacionAbsolutaMedia(listaDemandaPronosticada);

    // Mostrar el valor de la MAD
    // System.out.println("Desviación Absoluta Media (MAD): " + mad);

    // Cálculo de la MSE
    // Llamada al método
    // double mse = obtenerErrorCuadraticoMedio(listaDemandaPronosticada);

    // Mostrar el valor
    // System.out.println("Error cuadrático medio: " + mse);

    // Cálculo de la MAPE
    // Llamada al método
    // double mape = obtenerErrorMedioAbsolutoPorcentual(listaDemandaPronosticada);

    // Mostrar el valor1
    // System.out.println("Error Porcentual Absoluto Medio: " + mape);

    // entrada.close();
    // return listaDemandaPronosticada;
    // }

    // /**
    // * Este método calcula la demanda pronosticada utilizando el método de
    // Suavización Exponencial.
    // * El método de Suavización Exponencial es una técnica de pronóstico que
    // utiliza una combinación ponderada del valor de demanda real del periodo
    // anterior
    // * y el valor de demanda pronosticada del periodo anterior para predecir la
    // demanda del próximo periodo. El coeficiente 'alpha' determina el peso
    // relativo
    // * de la demanda real y la demanda pronosticada del periodo anterior.
    // *
    // * @param demandasHistoricas Una lista de demandas históricas seleccionadas.
    // * @return Una lista de demandas pronosticadas.
    // */
    // public List<DemandaPronosticada> suavizacionExponencial(List<Demanda>
    // demandasHistoricas) {
    // Scanner entrada = new Scanner(System.in);

    // System.out.println("Método de predicción Suavización Exponencial");

    // int nroAnio = 0;
    // while (true) {
    // System.out.println("Ingrese el año de demanda histórica: ");
    // if (entrada.hasNextInt()) {
    // nroAnio = entrada.nextInt();
    // if (nroAnio > 0) {
    // break;
    // } else {
    // System.out.println("El año debe ser un número positivo.");
    // }
    // } else {
    // System.out.println("Por favor, ingrese un número válido.");
    // entrada.next(); // Descarta la entrada incorrecta
    // }
    // }

    // List<Demanda> listaDemandaSeleccionadas =
    // seleccionarDemandaHistoricas(demandasHistoricas, nroAnio);
    // List<DemandaPronosticada> demandasPronosticadas = new ArrayList<>();

    // double alpha = 0;
    // while (true) {
    // System.out.println(
    // "\nIngrese el valor del coeficiente alpha (utilice coma como separador
    // decimal) Siendo valores cercanos a cero para hechos fortuitos (inciertos) o
    // cercanos a uno para aleatorios: ");
    // if (entrada.hasNextDouble()) {
    // alpha = entrada.nextDouble();
    // if (alpha > 0 && alpha < 1) {
    // break;
    // } else {
    // System.out.println("El coeficiente alpha debe ser mayor que cero y menor que
    // uno.");
    // }
    // } else {
    // System.out.println("Por favor, ingrese un número válido utilizando la coma
    // como separador decimal.");
    // entrada.next(); // Descarta la entrada incorrecta
    // }
    // }

    // System.out.println("Opciones: ");
    // System.out.println(
    // "1- Ingresar manualmente la cantidad demandada real del período anterior al
    // que desea predecir.");
    // System.out.println("2- Calcular suavización exponencial para los períodos del
    // año ingresado. Año: " + nroAnio);

    // int seleccion = 0;
    // while (true) {
    // if (entrada.hasNextInt()) {
    // seleccion = entrada.nextInt();
    // if (seleccion == 1 || seleccion == 2) {
    // break;
    // } else {
    // System.out.println("Por favor, seleccione una opción válida (1 o 2).");
    // }
    // } else {
    // System.out.println("Por favor, ingrese un número válido.");
    // entrada.next(); // Descarta la entrada incorrecta
    // }
    // }

    // if (seleccion == 1) {
    // int nroPeriodo = 0;
    // int mesDemandaAnterior = nroPeriodo - 1;
    // int cantidadRealDemandadaAnterior = 0;
    // double cantidadDemandadaPronosticadaAnterior = 0;

    // while (true) {
    // System.out.println("Ingrese el número de período que desea predecir: ");
    // if (entrada.hasNextInt()) {
    // nroPeriodo = entrada.nextInt();
    // if (nroPeriodo > 1) {
    // break;
    // } else {
    // System.out.println("El período debe ser mayor a uno.");
    // }
    // } else {
    // System.out.println("Por favor, ingrese un número válido.");
    // entrada.next(); // Descarta la entrada incorrecta
    // }
    // }

    // while (true) {
    // System.out.println(
    // "\nIngrese la cantidad demandada real correspondiente al período " +
    // mesDemandaAnterior + ": ");
    // if (entrada.hasNextInt()) {
    // cantidadRealDemandadaAnterior = entrada.nextInt();
    // if (cantidadRealDemandadaAnterior >= 0) {
    // break;
    // } else {
    // System.out.println("La cantidad demandada debe ser un número no negativo.");
    // }
    // } else {
    // System.out.println("Por favor, ingrese un número válido.");
    // entrada.next(); // Descarta la entrada incorrecta
    // }
    // }

    // while (true) {
    // System.out.println("\nIngrese la cantidad demandada pronosticada
    // correspondiente al período "
    // + mesDemandaAnterior + ": ");
    // if (entrada.hasNextDouble()) {
    // cantidadDemandadaPronosticadaAnterior = entrada.nextDouble();
    // if (cantidadDemandadaPronosticadaAnterior >= 0) {
    // break;
    // } else {
    // System.out.println("La cantidad demandada pronosticada debe ser un número no
    // negativo.");
    // }
    // } else {
    // System.out.println("Por favor, ingrese un número válido.");
    // entrada.next(); // Descarta la entrada incorrecta
    // }
    // }

    // double prediccionSuavizacionExponencial =
    // cantidadDemandadaPronosticadaAnterior
    // + alpha * (cantidadRealDemandadaAnterior -
    // cantidadDemandadaPronosticadaAnterior);
    // DemandaPronosticada dp = new DemandaPronosticada();
    // dp.setAnioDemandaPronosticada(nroAnio);
    // dp.setCantidadDemandadaPronostico(prediccionSuavizacionExponencial);
    // dp.setNroPeriodoDemandaPronosticada(nroPeriodo);
    // dp.setDemandaRealAsociada(demandasHistoricas.get(mesDemandaAnterior - 1));
    // demandasPronosticadas.add(dp);

    // System.out.println("Demanda pronosticada del período anterior " +
    // mesDemandaAnterior + ": "
    // + cantidadDemandadaPronosticadaAnterior);
    // System.out.println(
    // "Demanda real del período anterior " + mesDemandaAnterior + ": " +
    // cantidadRealDemandadaAnterior);
    // System.out.println("Demanda pronosticada para el período " +
    // dp.getNroPeriodoDemandaPronosticada() + ": "
    // + dp.getCantidadDemandadaPronostico());

    // } else {
    // double cantidadDemandadaPronosticadaPrimerPeriodo = 0;
    // while (true) {
    // System.out.println("\nIngrese la cantidad demandada pronosticada
    // correspondiente al período 1: ");
    // if (entrada.hasNextDouble()) {
    // cantidadDemandadaPronosticadaPrimerPeriodo = entrada.nextDouble();
    // if (cantidadDemandadaPronosticadaPrimerPeriodo >= 0) {
    // break;
    // } else {
    // System.out.println("La cantidad demandada pronosticada debe ser un número no
    // negativo.");
    // }
    // } else {
    // System.out.println("Por favor, ingrese un número válido.");
    // entrada.next(); // Descarta la entrada incorrecta
    // }
    // }

    // DemandaPronosticada dp = new DemandaPronosticada();
    // dp.setCantidadDemandadaPronostico(cantidadDemandadaPronosticadaPrimerPeriodo);
    // dp.setAnioDemandaPronosticada(nroAnio);
    // dp.setNroPeriodoDemandaPronosticada(1);
    // dp.setDemandaRealAsociada(demandasHistoricas.get(0));
    // demandasPronosticadas.add(dp);

    // System.out.printf("%-10s%-25s%-25s%n", "Período", "Unidades", "Con alpha = "
    // + alpha);
    // System.out.printf("%-10d%-25d%-25.2f%n", 1,
    // demandasHistoricas.get(0).getCantidadTotalDemandada(),
    // cantidadDemandadaPronosticadaPrimerPeriodo);

    // for (int i = 1; i < listaDemandaSeleccionadas.size(); i++) {
    // double prediccionSuavizacionExponencial = demandasPronosticadas.get(i - 1)
    // .getCantidadDemandadaPronostico()
    // + alpha * (listaDemandaSeleccionadas.get(i - 1).getCantidadTotalDemandada()
    // - demandasPronosticadas.get(i - 1).getCantidadDemandadaPronostico());

    // dp = new DemandaPronosticada();
    // dp.setCantidadDemandadaPronostico(prediccionSuavizacionExponencial);
    // dp.setAnioDemandaPronosticada(nroAnio);
    // dp.setNroPeriodoDemandaPronosticada(i + 1);
    // if (i < demandasHistoricas.size()) {
    // dp.setDemandaRealAsociada(demandasHistoricas.get(i));
    // } else {
    // dp.setDemandaRealAsociada(null);
    // }

    // demandasPronosticadas.add(dp);

    // System.out.printf("%-10d%-25d%-25.2f%n",
    // dp.getNroPeriodoDemandaPronosticada(),
    // listaDemandaSeleccionadas.get(i).getCantidadTotalDemandada(),
    // demandasPronosticadas.get(i).getCantidadDemandadaPronostico());
    // }

    // Añadiendo la predicción para el siguiente período
    // int ultimoPeriodo = demandasPronosticadas.get(demandasPronosticadas.size() -
    // 1)
    // .getNroPeriodoDemandaPronosticada();
    // double ultimaDemandaPronosticada =
    // demandasPronosticadas.get(demandasPronosticadas.size() - 1)
    // .getCantidadDemandadaPronostico();
    // double ultimaDemandaReal =
    // listaDemandaSeleccionadas.get(listaDemandaSeleccionadas.size() - 1)
    // .getCantidadTotalDemandada();
    // double prediccionProximoPeriodo = ultimaDemandaPronosticada
    // + alpha * (ultimaDemandaReal - ultimaDemandaPronosticada);

    // System.out.printf("%-10d%-25s%-25.2f%n",
    // ultimoPeriodo + 1,
    // "?",
    // prediccionProximoPeriodo);

    // double mad = obtenerDesviacionAbsolutaMedia(demandasPronosticadas);
    // double mse = obtenerErrorCuadraticoMedio(demandasPronosticadas);
    // double mape = obtenerErrorMedioAbsolutoPorcentual(demandasPronosticadas);

    // System.out.printf("MAD (Desviación Absoluta Media): %.2f%n", mad);
    // System.out.printf("MSE (Error Cuadrático Medio): %.2f%n", mse);
    // System.out.printf("MAPE (Error Medio Absoluto Porcentual): %.4f%n", mape);
    // }

    // entrada.close();
    // return demandasPronosticadas;
    // }

    // /**
    // * Este método calcula la demanda pronosticada utilizando el método de
    // Pronóstico Estacional.
    // * El método de Pronóstico Estacional es una técnica de pronóstico que utiliza
    // la demanda histórica de varios años
    // * para predecir la demanda del próximo año. Este método es útil cuando la
    // demanda tiene una tendencia estacional.
    // *
    // * @param demandasHistoricas Una lista de demandas históricas.
    // * @return Una lista de demandas pronosticadas.
    // */
    // public List<DemandaPronosticada> pronosticoEstacional(List<Demanda>
    // demandasHistoricas) {
    // Scanner entrada = new Scanner(System.in);
    // List<DemandaPronosticada> demandasPronosticadas = new ArrayList<>();
    // boolean bandera = false;
    // int anioInicio = 0;
    // int anioFin = 0;

    // Validación del año a predecir
    // int anioAPredecir;
    // while (true) {
    // try {
    // System.out.println("Ingrese el número del año el cuál desea predecir la
    // demanda: ");
    // anioAPredecir = entrada.nextInt();
    // if (anioAPredecir > 0)
    // break;
    // else
    // System.out.println("Por favor, ingrese un año válido.");
    // } catch (Exception e) {
    // System.out.println("Entrada no válida. Intente nuevamente.");
    // entrada.next(); // Clear invalid input
    // }
    // }

    // Validación del rango de años
    // while (!bandera) {
    // try {
    // System.out.println(
    // "A continuación, ingrese el rango de años que van a ser utilizados para la
    // predicción con demanda estacional. [Año inicio; año fin]. Año fin debe ser el
    // año anterior al año a predecir la demanda.");
    // System.out.println("Ingrese año inicio: ");
    // anioInicio = entrada.nextInt();
    // System.out.println("Ingrese año fin: ");
    // anioFin = entrada.nextInt();

    // if (anioInicio > 0 && anioFin > 0 && anioFin == anioAPredecir - 1) {
    // bandera = true;
    // } else {
    // System.out.println(
    // "El rango de fechas ingresado no es correcto. Año fin debe ser el año
    // anterior al año a predecir la demanda.");
    // }
    // } catch (Exception e) {
    // System.out.println("Entrada no válida. Intente nuevamente.");
    // entrada.next(); // Clear invalid input
    // }
    // }

    // int cantidadAnios = anioFin - anioInicio + 1;
    // int[] anios = new int[cantidadAnios];

    // for (int i = 0; i < cantidadAnios; i++) {
    // anios[i] = anioInicio + i;
    // }

    // List<List<Demanda>> demandasSeleccionadasPorAnio = new ArrayList<>();

    // for (int i = 0; i < cantidadAnios; i++) {
    // List<Demanda> demandasAnio = seleccionarDemandaHistoricas(demandasHistoricas,
    // anios[i]);

    // if (demandasAnio.size() != 12) {
    // System.out.println("Error: El año " + anios[i] + " no tiene 12 registros de
    // demanda.");
    // entrada.close();
    // return demandasPronosticadas;
    // }

    // demandasSeleccionadasPorAnio.add(demandasAnio);
    // }

    // for (int i = 0; i < cantidadAnios; i++) {
    // for (int j = 0; j < 12; j++) {
    // System.out.println("Año: " +
    // demandasSeleccionadasPorAnio.get(i).get(j).getAnioDemanda() + ". Mes: "
    // + (j + 1) + ". Cantidad demandada = "
    // + demandasSeleccionadasPorAnio.get(i).get(j).getCantidadTotalDemandada());
    // }
    // }

    // Validación de la demanda esperada
    // int demandaEsperada;
    // while (true) {
    // try {
    // System.out.println("Ingrese la demanda esperada para el año: " +
    // anioAPredecir);
    // demandaEsperada = entrada.nextInt();
    // if (demandaEsperada > 0)
    // break;
    // else
    // System.out.println("Por favor, ingrese una demanda válida.");
    // } catch (Exception e) {
    // System.out.println("Entrada no válida. Intente nuevamente.");
    // entrada.next(); // Clear invalid input
    // }
    // }

    // int suma = 0;
    // int contador = 0;

    // for (int i = 0; i < cantidadAnios; i++) {
    // for (int j = 0; j < 12; j++) {
    // suma +=
    // demandasSeleccionadasPorAnio.get(i).get(j).getCantidadTotalDemandada();
    // contador++;
    // }
    // }

    // double promedioTotal = (double) suma / contador;

    // double[][] indicesDemandaPorAnio = new double[12][cantidadAnios];

    // for (int k = 0; k < cantidadAnios; k++) {
    // for (int j = 0; j < 12; j++) {
    // indicesDemandaPorAnio[j][k] =
    // demandasSeleccionadasPorAnio.get(k).get(j).getCantidadTotalDemandada()
    // / promedioTotal;
    // }
    // }

    // double[] indiceEstacionalidad = new double[12];
    // double suma2 = 0;

    // for (int j = 0; j < 12; j++) {
    // for (int k = 0; k < cantidadAnios; k++) {
    // suma2 += indicesDemandaPorAnio[j][k];
    // }
    // indiceEstacionalidad[j] = suma2 / cantidadAnios;
    // suma2 = 0;
    // }

    // double sumaIndiceEstacionalidad = 0;
    // for (double v : indiceEstacionalidad) {
    // sumaIndiceEstacionalidad += v;
    // }

    // System.out.println("Suma índice estacionalidad: " +
    // Math.round(sumaIndiceEstacionalidad));

    // for (int j = 0; j < 12; j++) {
    // DemandaPronosticada dp = new DemandaPronosticada();

    // int cantidadDemandadaPredecida = (int) Math.round(promedioTotal *
    // indiceEstacionalidad[j]);

    // dp.setCantidadDemandadaPronostico(cantidadDemandadaPredecida);
    // dp.setAnioDemandaPronosticada(anioAPredecir);
    // dp.setNroPeriodoDemandaPronosticada(j + 1);

    // demandasPronosticadas.add(dp);

    // System.out.println("Demanda pronosticada para el mes " + (j + 1) + " del año
    // " + anioAPredecir + ": "
    // + demandasPronosticadas.get(j).getCantidadDemandadaPronostico());
    // }

    // entrada.close();
    // return demandasPronosticadas;
    // }

    // /**
    // * Este método calcula las ponderaciones para el método de Promedio Ponderado.
    // * El método de Promedio Ponderado es una técnica de pronóstico que utiliza el
    // promedio ponderado de los 'n' periodos anteriores
    // * para predecir la demanda del próximo periodo. Las ponderaciones se calculan
    // de tal manera que los periodos más recientes tienen
    // * un mayor peso en el cálculo del promedio.
    // *
    // * @param n La cantidad de períodos históricos a considerar para el cálculo.
    // * @return Un array de doubles que contiene las ponderaciones para cada
    // periodo histórico.
    // */
    // private double[] calcularPonderaciones(int n) {
    // double[] ponderaciones = new double[n];
    // double sumatoria = 0;
    // for (int i = 0; i < n; i++) {
    // sumatoria += (i + 1);
    // }
    // for (int i = 0; i < n; i++) {
    // ponderaciones[i] = (i + 1) / sumatoria;
    // }
    // System.out.println("Ponderaciones calculadas: " +
    // Arrays.toString(ponderaciones));
    // return ponderaciones;
    // }

    // public List<DemandaPronosticada> predecirDemanda(List<Demanda> demandas) {

    // Scanner entrada = new Scanner(System.in);

    // System.out.println("\nOpciones: ");
    // System.out.println("1- Promedio Móvil ");
    // System.out.println("2- Promedio Ponderado \n");
    // System.out.println("3- Suavización Exponencial \n");
    // System.out.println("4- Demanda Estacional\n");

    // int seleccion = 0;
    // String nombreMetodoPrediccion = "Nada seleccionado";

    // while (seleccion == 0) {
    // System.out.println("Seleccione metodo predicción: ");
    // seleccion = entrada.nextInt();
    // switch (seleccion) {
    // case 1:
    // nombreMetodoPrediccion = "Promedio Móvil";
    // break;
    // case 2:
    // nombreMetodoPrediccion = "Promedio Ponderado";
    // break;
    // case 3:
    // nombreMetodoPrediccion = "Suavización Exponencial";
    // break;
    // case 4:
    // nombreMetodoPrediccion = "Demanda Estacional";
    // break;
    // default:
    // System.out.println("No ha seleccionado una opción válida.");
    // nombreMetodoPrediccion = "Nada seleccionado";
    // seleccion = 0;
    // break;
    // }
    // }

    // if (nombreMetodoPrediccion.equals("Promedio Móvil")) {
    // this.demandaPronosticada = promedioMovil(demandas);
    // } else if (nombreMetodoPrediccion.equals("Promedio Ponderado")) {
    // this.demandaPronosticada = promedioPonderado(demandas);
    // } else if (nombreMetodoPrediccion.equals("Suavización Exponencial")) {
    // this.demandaPronosticada = suavizacionExponencial(demandas);
    // } else if (nombreMetodoPrediccion.equals("Demanda Estacional")) {
    // this.demandaPronosticada = pronosticoEstacional(demandas);
    // } else {
    // System.out.println("No ha seleccionado una opción válida.");
    // }

    // entrada.close();
    // return this.demandaPronosticada;
    // }

    // public int getNumPronostico() {
    // return numPronostico;
    // }

    // public String getNombrePronostico() {
    // return nombrePronostico;
    // }

    // public double getErrorAceptable() {
    // return errorAceptable;
    // }

    // public int getCantidadPeriodosAPredecir() {
    // return cantidadPeriodosAPredecir;
    // }

    // public int getCantidadPeriodosHistoricos() {
    // return cantidadPeriodosHistoricos;
    // }

    // public void setNombrePronostico(String nombrePronostico) {
    // Ensure the input is not null to avoid NullPointerException
    // if (nombrePronostico != null) {
    // this.nombrePronostico = nombrePronostico;
    // } else {
    // Handle the null case or throw an IllegalArgumentException
    // This is a simple fix to ensure the field is not set to null inadvertently.
    // throw new IllegalArgumentException("nombrePronostico cannot be null");
    // }
    // }

    // public void setErrorAceptable(double errorAceptable) {
    // if (errorAceptable < 0) {
    // throw new IllegalArgumentException("errorAceptable cannot be negative");
    // }
    // this.errorAceptable = errorAceptable;
    // }

    // public void setCantidadPeriodosAPredecir(int cantidadPeriodosAPredecir) {
    // if (cantidadPeriodosAPredecir <= 0) {
    // throw new IllegalArgumentException("cantidadPeriodosAPredecir must be
    // positive");
    // }
    // this.cantidadPeriodosAPredecir = cantidadPeriodosAPredecir;
    // }

    // public void setCantidadPeriodosHistoricos(int cantidadPeriodosHistoricos) {
    // if (cantidadPeriodosHistoricos <= 0) {
    // throw new IllegalArgumentException("cantidadPeriodosHistoricos must be
    // positive");
    // }
    // this.cantidadPeriodosHistoricos = cantidadPeriodosHistoricos;
    // }

    // public Articulo getArticulo() {
    // return articulo;
    // }

    // public void setArticulo(Articulo articulo) {
    // this.articulo = articulo;
    // }

    // }

    // FIn código java
   

    
}
