package com.example.demo.controller;

import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.services.DemandaPronosticadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demandapronosticada")
public class DemandaPronosticadaController {

    @Autowired
    private DemandaPronosticadaService demandaPronosticadaService;

    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        try {
            List<DemandaPronosticada> demandasPronosticadas = demandaPronosticadaService.findAll();
            return new ResponseEntity<>(demandasPronosticadas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            DemandaPronosticada demandaPronosticada = demandaPronosticadaService.findById(id);
            return new ResponseEntity<>(demandaPronosticada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody DemandaPronosticada demandaPronosticada) {
        try {
            DemandaPronosticada createdDemandaPronosticada = demandaPronosticadaService.save(demandaPronosticada);
            return new ResponseEntity<>(createdDemandaPronosticada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DemandaPronosticada demandaPronosticada) {
        try {
            DemandaPronosticada updatedDemandaPronosticada = demandaPronosticadaService.update(id, demandaPronosticada);
            return new ResponseEntity<>(updatedDemandaPronosticada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            demandaPronosticadaService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}