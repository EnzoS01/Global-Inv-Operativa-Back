package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entities.Demanda;
import com.example.demo.services.DemandaServiceImpl;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/demandas")
public class DemandaController extends BaseControllerImpl<Demanda,DemandaServiceImpl>{

    @Autowired
    private DemandaServiceImpl demandaService;


    @PostMapping("/setearArticulo/{demandaId}/{articuloId}")
    public ResponseEntity<?> setearArticulo(@PathVariable Long demandaId,@PathVariable Long articuloId){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(demandaService.setArticulo(demandaId,articuloId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }

    }

    @PostMapping("/setearDetallesVenta/{demandaId}")
    public ResponseEntity<?> setearDetallesVenta(@PathVariable Long demandaId){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(demandaService.setDetallesVenta(demandaId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/setearTotalDemanda/{demandaId}")
    public ResponseEntity<?> setearTotalDemanda(@PathVariable Long demandaId){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(demandaService.calcularTotalDemandada(demandaId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

}


