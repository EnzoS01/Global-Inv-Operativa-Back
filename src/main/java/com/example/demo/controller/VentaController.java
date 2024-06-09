package com.example.demo.controller;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.Cliente;
import com.example.demo.entities.DetalleVenta;
import com.example.demo.entities.Venta;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.ClienteRepository;
import com.example.demo.services.VentaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/ventas")
public class VentaController extends BaseControllerImpl<Venta, VentaServiceImpl>{

    @Autowired
    VentaServiceImpl ventaService;
    @Autowired
    ClienteRepository ClienteRepo;
    @Autowired
    ArticuloRepository ArticuloRepo;

    //este metodo listaria todas las ventas que estan cargadas
    @GetMapping("/disponibles")
    public ResponseEntity<?> VerVentas(){
        try{
            List<Venta> ventas = ventaService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(ventas);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    } //YA SE HACE ESTO CON EL METODO GENERICO getAll()*/


    @GetMapping("/nueva")
    public ResponseEntity<?> ObtenerDatosNueva(){
        try{
            List<Cliente> clientes = ClienteRepo.findAll();
            List<Articulo> articulos = ArticuloRepo.findAllDisponibles(); //TAMBIEN SE PUEDE HACER CON EL findAll que provee JPARepository
            return ResponseEntity.status(HttpStatus.OK).body(new Object[]{clientes, articulos});
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> CrearNueva(@RequestBody Venta venta){ //faltan parametros
        try{
            Venta nuevaVenta = ventaService.save(venta);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @PostMapping("/agregarDetalle/{ventaId}") //Con este metodo se agrega un detalleVenta a una venta existente, revisar VentaServiceImpl
    public ResponseEntity<?> agregarDetalleVenta(@PathVariable Long ventaId, @RequestBody DetalleVenta detalleVenta){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ventaService.agregarDetalleVenta(ventaId,detalleVenta));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    //recupero una venta existente para modificarla
    @GetMapping("/{id}")
    public ResponseEntity<?> RecuperarVenta(@PathVariable Long id){  /////HACE LO MISMO QUE getOne GENERICO
        try{
            Venta venta = ventaService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(venta);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }
    //guardo los cambios realizados sobre la venta
    @PutMapping("/{id]")
    public ResponseEntity<?> ActualizarVenta(@PathVariable Long id, @RequestBody Venta venta){//HACE LO MISMO QUE update GENERICO
        try{
            Venta ventaExistente = ventaService.findById(id);
            venta.setId(id);
            Venta ventaActualizada = ventaService.save(venta);
            return ResponseEntity.status(HttpStatus.OK).body(ventaActualizada);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    //eliminar una venta
    @DeleteMapping("{id}")
    public ResponseEntity<?> BorrarVenta(@PathVariable Long id){  //HACE LO MISMO QUE delete GENERICO
        try{
            ventaService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("La venta se elimino");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }

    }

}
