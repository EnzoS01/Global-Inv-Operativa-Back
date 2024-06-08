package com.example.demo.controller;

import com.example.demo.entities.Cliente;
import com.example.demo.services.ClienteServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/clientes")
public class ClienteController extends BaseControllerImpl<Cliente, ClienteServiceImpl>{
}
