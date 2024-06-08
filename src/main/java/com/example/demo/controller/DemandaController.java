package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Demanda;
import com.example.demo.services.DemandaServiceImpl;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/demandas")
public class DemandaController extends BaseControllerImpl<Demanda,DemandaServiceImpl>{

}


