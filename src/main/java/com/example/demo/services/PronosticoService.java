package com.example.demo.services;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.Demanda;
import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.entities.Pronostico;

import java.util.List;

public interface PronosticoService extends BaseService<Pronostico, Long> {

    public Pronostico asignarArticulo(Long pronosticoId,Long articuloId) throws Exception;

    public Pronostico asignarMetodoError(Long pronosticoId, Long metodoErrorId) throws Exception;

    public Pronostico promedioPonderado(Long pronosticoId,double factorPonderacion) throws Exception;

    public Pronostico pmSuavizado(Long pronosticoId, double predecidaRaiz, double valorCoeficiente) throws Exception;

    public Pronostico regresionLineal(Long pronosticoId) throws Exception;

    public Pronostico pronosticoEstacionalidad(Long pronosticoId, double demandaEsperada) throws Exception;

    public Pronostico generarOrdenCompra(Long pronosticoId) throws Exception;

    public Pronostico asignarDemandaReal(Long pronosticoId) throws Exception;

    public Pronostico calcularError(Long pronosticoId) throws Exception;
}
