package com.example.demo.controller;

import com.example.demo.entities.Articulo;
import com.example.demo.services.ArticuloService;
import com.example.demo.services.ArticuloServiceImpl;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/articulos")
public class ArticuloController extends BaseControllerImpl<Articulo, ArticuloServiceImpl> {
    @Autowired
    protected ArticuloService articuloservice;
    @PostMapping("/agregarProveedorPredeterminado/{idArticulo}/{idProveedor}")
    public ResponseEntity<?> setProveedorPredeterminado(@PathVariable Long idArticulo,@PathVariable Long idProveedor){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articuloservice.agregarProveedorPredeterminado(idArticulo,idProveedor));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
    @PostMapping("/agregarModelo/{idArticulo}/{idModelo}")
    public ResponseEntity<?> setModelo(@PathVariable Long idArticulo,@PathVariable Long idModelo){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articuloservice.agregarProveedorPredeterminado(idArticulo,idModelo));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
    
    @PostMapping("/agregarCGIConProveedorPredeterminado/{idArticulo}/{añoDesde}/{añoHasta}/{periodoDesde}/{periodoHasta}")
    public ResponseEntity<?> setCGIConProveedorPredeterminado(@PathVariable Long idArticulo,@PathVariable int añoDesde ,@PathVariable int añoHasta ,@PathVariable int periodoDesde ,@PathVariable int periodoHasta){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articuloservice.calcularCGIConProvPredeterminado(idArticulo, añoDesde, añoHasta, periodoDesde, periodoHasta));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/agregarCGI/{idArticulo}/{añoDesde}/{añoHasta}/{periodoDesde}/{periodoHasta}/{idProveedor}")
    public ResponseEntity<?> setCGI(@PathVariable Long idArticulo,@PathVariable int añoDesde ,@PathVariable int añoHasta ,@PathVariable int periodoDesde ,@PathVariable int periodoHasta, @PathVariable Long idProveedor){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articuloservice.calcularCGI(idArticulo, añoDesde, añoHasta, periodoDesde, periodoHasta,idProveedor));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/agregarProveedor/{tiempoPedidoMinutos}/{costoPedido}/{costoAlmacenamiento}/{cotoProducto}/{idArticulo}/{idProveedor}")
    public ResponseEntity<?> setProveedor(@PathVariable long tiempoPedidoMinutos,@PathVariable float costoPedido,@PathVariable float costoAlmacenamiento,@PathVariable float costoProducto,@PathVariable Long idArticulo,@PathVariable Long idProveedor){
        try {
            Duration duracionMinutos = Duration.ofMinutes(tiempoPedidoMinutos);
            return ResponseEntity.status(HttpStatus.OK).body(articuloservice.AsignarUnProveedorAUnArticulo(duracionMinutos, costoPedido, costoAlmacenamiento, costoProducto, idArticulo, idProveedor));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
}
