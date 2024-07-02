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
@CrossOrigin(origins = "*")
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

    @PostMapping("/asignarMetodo/{pronosticoId}/{metodoErrorId}")
    public ResponseEntity<?> asignarMetodo(@PathVariable Long pronosticoId, @PathVariable Long metodoErrorId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.asignarMetodoError(pronosticoId,metodoErrorId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/generarOrdenCompra/{pronosticoId}")
    public ResponseEntity<?> generarOrdenCompra(@PathVariable Long pronosticoId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.generarOrdenCompra(pronosticoId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }


    @PostMapping("/calcularPromedioPonderado/{pronosticoId}/{factorPonderacion}")
    public ResponseEntity<?> calcularPromedioPonderado(@PathVariable Long pronosticoId, @PathVariable double factorPonderacion) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.promedioPonderado(pronosticoId,factorPonderacion));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/calcularPMSuavizado/{pronosticoId}/{predecidaRaiz}/{valorCoeficiente}")
    public ResponseEntity<?> calcularPMSuavizado(@PathVariable Long pronosticoId,@PathVariable double predecidaRaiz,@PathVariable double valorCoeficiente) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.pmSuavizado(pronosticoId,predecidaRaiz,valorCoeficiente));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/calcularRegresionLineal/{pronosticoId}")
    public ResponseEntity<?> calcularRegresionLineal(@PathVariable Long pronosticoId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.regresionLineal(pronosticoId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/calcularPronosticoEstacionalidad/{pronosticoId}/{demandaEsperada}")
    public ResponseEntity<?> calcularPronosticoEstacionalidad(@PathVariable Long pronosticoId,@PathVariable double demandaEsperada) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.pronosticoEstacionalidad(pronosticoId,demandaEsperada));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
    @PostMapping("/asignarDemanda/{pronosticoId}")
    public ResponseEntity<?> asignarDemandaReal(@PathVariable Long pronosticoId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.asignarDemandaReal(pronosticoId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/calcularError/{pronosticoId}")
    public ResponseEntity<?> calcularError(@PathVariable Long pronosticoId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.calcularError(pronosticoId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }


    @PostMapping("/calcularErrorMAD/{pronosticoId}")
    public ResponseEntity<?> calcularErrorMAD(@PathVariable Long pronosticoId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.calcularErrorMAD(pronosticoId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/calcularErrorMSE/{pronosticoId}")
    public ResponseEntity<?> calcularErrorMSE(@PathVariable Long pronosticoId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.calcularErrorMSE(pronosticoId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/calcularErrorMAPE/{pronosticoId}")
    public ResponseEntity<?> calcularErrorMAPE(@PathVariable Long pronosticoId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pronosticoService.calcularErrorMAPE(pronosticoId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

}
