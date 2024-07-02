package com.example.demo.controller;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.services.ArticuloServiceImpl;
import com.example.demo.services.DemandaPronosticadaService;
import com.example.demo.services.DemandaPronosticadaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/demandaspronosticadas")
public class DemandaPronosticadaController extends BaseControllerImpl<DemandaPronosticada, DemandaPronosticadaServiceImpl>{

    @Autowired
    private DemandaPronosticadaService demandaPronosticadaService;

}