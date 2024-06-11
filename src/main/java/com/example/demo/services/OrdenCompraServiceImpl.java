package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class OrdenCompraServiceImpl extends BaseServiceImpl<OrdenCompra,Long> implements OrdenCompraService {
    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private EstadoOrdenCompraRepository estadoOrdenRepository;

    @Autowired
    private ProveedorArticuloRepository ProveedorArticuloRepo;

    @Autowired
    private DetalleOrdenCompraRepository detalleOrdenCompraRepository;

    @Autowired
    private CambioOrdenCompraEstadoRepository CambioOrdenCompraEstadoRepo;

    @Autowired
    private ArticuloRepository ArticuloRepo;


    public OrdenCompraServiceImpl(BaseRepository<OrdenCompra, Long> baseRepository, OrdenCompraRepository ordenCompraRepository) {
        super(baseRepository);
        this.ordenCompraRepository = ordenCompraRepository;
    }

    public List<OrdenCompra> ListaOrdenes(){ //este metodo se encarga de recuperar las ordenes de compra pendientes o enviadas
        //busco los estados que necesito    //serian las ordenes de compra que se listarian al ingresar a la seccion correspondiente
        EstadoOrdenCompra estadoPendiente = estadoOrdenRepository.findByName("Pendiente");
        EstadoOrdenCompra estadoEnviada = estadoOrdenRepository.findByName("Enviada");

        //busco las ordenes de compra que esten en standBy
        List<OrdenCompra> ordenes = ordenCompraRepository.findByState(estadoPendiente.getId(),estadoEnviada.getId());

        return ordenes;
    }

    @Transactional
    public OrdenCompra agregarDetalleOrdenCompra(Long ordenCompraId, DetalleOrdenCompra detalleOrdenCompra) {
        OrdenCompra ordenCompra = ordenCompraRepository.findById(ordenCompraId)
                .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada"));

        EstadoOrdenCompra estadoOrdenCompraPen = estadoOrdenRepository.findByName("Pendiente");
        // .orElseThrow(() -> new RuntimeException("Estado Orden de compra no encontrada"));

        EstadoOrdenCompra estadoOrdenCompraEnv = estadoOrdenRepository.findByName("Enviada");

        // Verifico si existe una orden para el artículo
        List<OrdenCompra> ordenesActivas = ordenCompraRepository.findByArticuloAndEstado(detalleOrdenCompra.getArticulo().getId(), estadoOrdenCompraPen.getId(), estadoOrdenCompraEnv.getId());

        if (!ordenesActivas.isEmpty()) {
            throw new RuntimeException("Ya existe/n"+ ordenesActivas.toArray().length + " orden/es de compra activa/s para el artículo " + detalleOrdenCompra.getArticulo().getNombreArticulo());
        }

        //necesito el precio del proveedor
        Proveedor p = detalleOrdenCompra.getProveedor();
        ProveedorArticulo PA = ProveedorArticuloRepo.findByArticuloandProveedor(p.getId(),detalleOrdenCompra.getArticulo().getId());

        // Calculo el subtotal
        detalleOrdenCompra.setSubtotal(detalleOrdenCompra.getCantidad() * PA.getCostoPedido());

        ordenCompra.getDetallesOrdenCompra().add(detalleOrdenCompra);
        detalleOrdenCompraRepository.save(detalleOrdenCompra);
        ordenCompraRepository.save(ordenCompra);
        return ordenCompra;
    }

    @Transactional
    public OrdenCompra finalizarCreacion(Long ordenCompraId){
        OrdenCompra OC = ordenCompraRepository.getReferenceById(ordenCompraId);

        // Calculo el total de la orden
        double total = 0;
        for (DetalleOrdenCompra detalle : OC.getDetallesOrdenCompra()) {
            total += detalle.getSubtotal();
        }
        OC.setTotal(total);
        ordenCompraRepository.save(OC);
        return OC;
    }

    @Transactional
    public OrdenCompra ActualizarEstado(Long ordenCompraId){
        OrdenCompra OC = ordenCompraRepository.getReferenceById(ordenCompraId);

        //busco el estado "Recibida"
        EstadoOrdenCompra estadoRecibida = estadoOrdenRepository.findByName("Recibida");

        //seteo nuevo estado a OC
        OC.setEstadoOrdenCompra(estadoRecibida);

        //documento el cambio de estado
        CambioOrdenCompraEstado CambioEstado = new CambioOrdenCompraEstado();
        CambioEstado.setFechaCambio(Date.from(Instant.now()));
        CambioEstado.setOrdenCompra(OC);
        CambioEstado.setEstadoOrdenCompra(estadoRecibida);
        CambioOrdenCompraEstadoRepo.save(CambioEstado);

        /*incremento el inventario de cada producto en la oc (suponiendo q llega todo ok)*/
        for (DetalleOrdenCompra detalle : OC.getDetallesOrdenCompra()) {
            Articulo a = detalle.getArticulo();
            a.setCantActual(a.getCantActual() + detalle.getCantidad() + a.getStockSeguridad());
            ArticuloRepo.save(a);
        }
        return OC;
    }
}
