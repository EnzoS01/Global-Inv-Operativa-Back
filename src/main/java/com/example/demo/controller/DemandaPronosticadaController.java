package com.example.demo.controller;

import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.services.DemandaPronosticadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/demandaspronosticada")
public class DemandaPronosticadaController {

    @Autowired
    private DemandaPronosticadaService demandaPronosticadaService;

    @PostMapping("/asignarDemanda/{demandaPronosticadaId}/{demandaId}")
    public ResponseEntity<?> asignarDemanda(@PathVariable Long demandaPronosticadaId, @PathVariable Long demandaId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(demandaPronosticadaService.asignarDemanda(demandaPronosticadaId,demandaId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
}