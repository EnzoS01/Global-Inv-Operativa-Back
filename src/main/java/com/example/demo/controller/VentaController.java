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

   /* //este metodo listaria todas las ventas que estan cargadas
    @GetMapping("/disponibles")
    public String VerVentas(Model model){
        List<Venta> ventas = service.ObtenerTodas();

        model.addAttribute("ventas",ventas);
    return "nombrevistaHTML"; //CAMBIAR ESTO
    } YA SE HACE ESTO CON EL METODO GENERICO getAll()*/

    @GetMapping("/nueva")
    public String CargarNueva(Model model){

        List<Cliente> clientes = ClienteRepo.findAll();
        List<Articulo> articulos = ArticuloRepo.findAllDisponibles();

        //te dejaria buscar el cliente al cual hacerle la venta con los productos
        model.addAttribute("clientes",clientes);
        model.addAttribute("articulos",articulos);

        return "vistaedicion"; //falta esto
    }

    @PostMapping("/nueva/guardar")
    public String CargarNueva(@RequestParam Date fecha){ //faltan parametros

        //pensando en los detalle venta

        return "redirect:/api/ventas/disponibles";
    }

    @PostMapping("/agregarDetalle/{ventaId}") //Con este metodo se agrega un detalleVenta a una venta existente, revisar VentaServiceImpl
    public ResponseEntity<?> agregarDetalleVenta(@PathVariable Long ventaId, @RequestBody DetalleVenta detalleVenta){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ventaService.agregarDetalleVenta(ventaId,detalleVenta));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

}
