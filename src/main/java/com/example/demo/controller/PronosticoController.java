package com.example.demo.controller;

import com.example.demo.entities.Demanda;
import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.entities.Pronostico;
import com.example.demo.services.PronosticoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pronostico")
public class PronosticoController extends BaseControllerImpl<Pronostico, PronosticoServiceImpl> {

    @Autowired
    private PronosticoServiceImpl pronosticoService;

    /*
    @PostMapping("/demanda")
    public ResponseEntity<List<DemandaPronosticada>> predecirDemanda(@RequestBody List<Demanda> demandas) {
        try {
            List<DemandaPronosticada> pronosticos = pronosticoService.predecirDemanda(demandas);
            return new ResponseEntity<>(pronosticos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @PostMapping("/calcularPromedioPonderado/{pronosticoId}/{anio}")
    public ResponseEntity<?> calcularPromedioPonderado(@PathVariable Long pronosticoId, @PathVariable int anio) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.promedioPonderado(pronosticoId,anio));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }


}
