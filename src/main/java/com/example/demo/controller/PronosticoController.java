package com.example.demo.controller;

import java.util.List;
import com.example.demo.entities.Demanda;
import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.services.PronosticoServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pronostico")
public class PronosticoController {

    @Autowired
    private PronosticoServiceImpl pronosticoService;

    @PostMapping("/demanda")
    public List<DemandaPronosticada> predecirDemanda(@RequestBody List<Demanda> demandas) {
        return pronosticoService.predecirDemanda(demandas);
    }

    // Otros métodos para operaciones específicas de pronósticos, como obtener
    // pronóstico por ID, etc.

}
