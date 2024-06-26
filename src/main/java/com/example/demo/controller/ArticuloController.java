package com.example.demo.controller;

import com.example.demo.DTO.ArticuloDTO;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/articulos")
public class ArticuloController extends BaseControllerImpl<Articulo, ArticuloServiceImpl> {
    @Autowired
    protected ArticuloService articuloservice;

    @PostMapping("/agregarProveedorPredeterminado/{tiempoPedidoDias}/{costoPedido}/{costoAlmacenamiento}/{costoProducto}/{idArticulo}/{idProveedor}")
    public ResponseEntity<?> setProveedorPredeterminado(@PathVariable("tiempoPedidoDias") int tiempoPedidoDias, @PathVariable("costoPedido") float costoPedido,@PathVariable("costoAlmacenamiento") float costoAlmacenamiento,@PathVariable("costoProducto") float costoProducto,@PathVariable("idArticulo") Long idArticulo,@PathVariable("idProveedor") Long idProveedor){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articuloservice.agregarProveedorPredeterminado(tiempoPedidoDias,costoPedido,costoAlmacenamiento,costoProducto,idArticulo,idProveedor));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
    @PostMapping("/agregarModelo/{idArticulo}/{idModelo}")
    public ResponseEntity<?> setModelo(@PathVariable("idArticulo") Long idArticulo,@PathVariable("idModelo") Long idModelo){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articuloservice.agregarModelo(idArticulo,idModelo));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
    @PostMapping("/agregarProveedor/{tiempoPedidoDias}/{costoPedido}/{costoAlmacenamiento}/{cotoProducto}/{idArticulo}/{idProveedor}")
    public ResponseEntity<?> setProveedor(@PathVariable("tiempoPedidoDias") int tiempoPedidoDias,@PathVariable("costoPedido") float costoPedido,@PathVariable("costoAlmacenamiento") float costoAlmacenamiento,@PathVariable("cotoProducto") float costoProducto,@PathVariable("idArticulo") Long idArticulo,@PathVariable("idProveedor") Long idProveedor){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articuloservice.AsignarUnProveedorAUnArticulo(tiempoPedidoDias, costoPedido, costoAlmacenamiento, costoProducto, idArticulo, idProveedor));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
    @PostMapping("/agregarCGIConProveedorPredeterminado/{idArticulo}/{añoDesde}/{añoHasta}/{periodoDesde}/{periodoHasta}")
    public ResponseEntity<?> setCGIConProveedorPredeterminado(@PathVariable("idArticulo") Long idArticulo,@PathVariable("añoDesde") int añoDesde ,@PathVariable("añoHasta") int añoHasta ,@PathVariable("periodoDesde") int periodoDesde ,@PathVariable("periodoHasta") int periodoHasta){
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
    @PostMapping("/ModeloConProveedor/{idArticulo}/{añoDesde}/{añoHasta}/{periodoDesde}/{periodoHasta}/{idProveedor}/{DDesvEstandar}/{Z}")
    public ResponseEntity<?> ModeloConProveedor(@PathVariable Long idArticulo,
                                      @PathVariable int añoDesde,
                                      @PathVariable int añoHasta,
                                      @PathVariable int periodoDesde,
                                      @PathVariable int periodoHasta,
                                      @PathVariable Long idProveedor,
                                      @PathVariable float DDesvEstandar,
                                      @PathVariable double Z){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articuloservice.ModeloConProveedor(idArticulo,
                                                                                              añoDesde,
                                                                                              añoHasta,
                                                                                              periodoDesde,
                                                                                              periodoHasta,
                                                                                              idProveedor,
                                                                                              DDesvEstandar,
                                                                                              Z
                                                                                              ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/ModeloConProveedorPredeterminado/{idArticulo}/{añoDesde}/{añoHasta}/{periodoDesde}/{periodoHasta}/{DDesvEstandar}/{Z}")
    public ResponseEntity<?> ModeloConProveedorPredeterminado(@PathVariable Long idArticulo,
                                      @PathVariable int añoDesde,
                                      @PathVariable int añoHasta,
                                      @PathVariable int periodoDesde,
                                      @PathVariable int periodoHasta,
                                      @PathVariable float DDesvEstandar,
                                      @PathVariable double Z){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articuloservice.ModeloConProveedorPredeterminado(idArticulo,
                                                                                              añoDesde,
                                                                                              añoHasta,
                                                                                              periodoDesde,
                                                                                              periodoHasta,
                                                                                              DDesvEstandar,
                                                                                              Z
                                                                                              ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
    

    @GetMapping("/ListadoArticulosFaltantes")
    public ResponseEntity<?> ListadoArticulosFaltantes(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articuloservice.ListadoDeArticulosFaltantes());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @GetMapping("/ListadoArticulosAReponer")
    public ResponseEntity<?> ListadoArticulosAReponer(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articuloservice.ListadoDeArticulosAReponer());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
}
