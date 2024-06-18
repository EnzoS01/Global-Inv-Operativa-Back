package com.example.demo.controller;

import java.util.List;

import com.example.demo.entities.Demanda;
import com.example.demo.entities.DemandaPronosticada;

import com.example.demo.services.PronosticoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pronostico")
public class PronosticoController {

    @Autowired
    private PronosticoServiceImpl pronosticoService;

    @PostMapping("/demanda")
    public ResponseEntity<List<DemandaPronosticada>> predecirDemanda(@RequestBody List<Demanda> demandas) {
        try {
            List<DemandaPronosticada> pronosticos = pronosticoService.predecirDemanda(demandas);
            return new ResponseEntity<>(pronosticos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandaPronosticada> getPronostico(@PathVariable Long id) {
        try {
            DemandaPronosticada pronostico = pronosticoService.getPronostico(id);
            if (pronostico != null) {
                return new ResponseEntity<>(pronostico, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DemandaPronosticada> updatePronostico(@PathVariable Long id, @RequestBody DemandaPronosticada pronosticoActualizado) {
        try {
            DemandaPronosticada pronostico = pronosticoService.updatePronostico(id, pronosticoActualizado);
            return new ResponseEntity<>(pronostico, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePronostico(@PathVariable Long id) {
        try {
            pronosticoService.deletePronostico(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<DemandaPronosticada>> getAllPronosticos() {
        try {
            List<DemandaPronosticada> pronosticos = pronosticoService.getAllPronosticos();
            return new ResponseEntity<>(pronosticos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
