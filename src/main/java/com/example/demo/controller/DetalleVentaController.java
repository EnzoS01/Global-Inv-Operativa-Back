package com.example.demo.controller;

import com.example.demo.entities.DetalleVenta;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.DetalleVentaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/detallesVenta")
public class DetalleVentaController extends BaseControllerImpl<DetalleVenta, DetalleVentaServiceImpl> {


}
