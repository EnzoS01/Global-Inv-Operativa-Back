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
@RequestMapping("api/pronosticos")
public class PronosticoController extends BaseControllerImpl<Pronostico, PronosticoServiceImpl> {

    @Autowired
    private PronosticoServiceImpl pronosticoService;


    @PostMapping("/asignarArticulo/{pronosticoId}/{articuloId}")
    public ResponseEntity<?> asignarArticulo(@PathVariable Long pronosticoId, @PathVariable Long articuloId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.asignarArticulo(pronosticoId,articuloId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }


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

    @PostMapping("/calcularPMSuavizado/{pronosticoId}/{predecidaRaiz}/{valorCoeficiente}/{anio}")
    public ResponseEntity<?> calcularPMSuavizado(@PathVariable Long pronosticoId,@PathVariable double predecidaRaiz,@PathVariable double valorCoeficiente, @PathVariable int anio) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.pmSuavizado(pronosticoId,predecidaRaiz,valorCoeficiente,anio));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/calcularRegresionLineal/{pronosticoId}/{anio}")
    public ResponseEntity<?> calcularRegresionLineal(@PathVariable Long pronosticoId, @PathVariable int anio) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.regresionLineal(pronosticoId,anio));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/calcularPronosticoEstacionalidad/{pronosticoId}/{anio}/{demandaEsperada}")
    public ResponseEntity<?> calcularPronosticoEstacionalidad(@PathVariable Long pronosticoId, @PathVariable int anio,@PathVariable double demandaEsperada) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.pronosticoEstacionalidad(pronosticoId,anio,demandaEsperada));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }



}
