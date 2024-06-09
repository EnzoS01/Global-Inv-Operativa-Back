package com.example.demo.services;

import com.example.demo.entities.EstadoOrdenCompra;
import com.example.demo.entities.OrdenCompra;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.EstadoOrdenCompraRepository;
import com.example.demo.repositories.OrdenCompraRepository;
import com.example.demo.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenCompraServiceImpl extends BaseServiceImpl<OrdenCompra,Long> implements OrdenCompraService {
    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private EstadoOrdenCompraRepository EstadoOrdenRepository;


    public OrdenCompraServiceImpl(BaseRepository<OrdenCompra, Long> baseRepository, OrdenCompraRepository ordenCompraRepository) {
        super(baseRepository);
        this.ordenCompraRepository = ordenCompraRepository;
    }

    public List<OrdenCompra> ListaOrdenes(){ //este metodo se encarga de recuperar las ordenes de compra pendientes o enviadas
        //busco los estados que necesito    //serian las ordenes de compra que se listarian al ingresar a la seccion correspondiente
        EstadoOrdenCompra estadoPendiente = EstadoOrdenRepository.findByName("Pendiente");
        EstadoOrdenCompra estadoEnviada = EstadoOrdenRepository.findByName("Enviada");

        //busco las ordenes de compra que esten en standBy
        List<OrdenCompra> ordenes = ordenCompraRepository.findByState(estadoPendiente.getId(),estadoEnviada.getId());

        return ordenes;
    }



}
