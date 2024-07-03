package com.example.demo.services;

import com.example.demo.DTO.DTOProveedor;
import com.example.demo.DTO.DTOProveedorArticulo;
import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
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

    public OrdenCompra crearUna(OrdenCompra orden){
        OrdenCompra nueva = new OrdenCompra();
        //nueva.setTotal(orden.getTotal());
        nueva.setEstadoOrdenCompra(orden.getEstadoOrdenCompra());
        nueva.setFechaRealizacion(orden.getFechaRealizacion());

        List<DetalleOrdenCompra> detalles = orden.getDetallesOrdenCompra();
        System.out.println("La longitud de los detalles es: " + detalles.size());


        List<DetalleOrdenCompra> nuevosdetalles = new ArrayList<>();
        float total = 0;

        for (DetalleOrdenCompra detalle : detalles) {
            try{
            System.out.println("Estoy dentro del for ");
            System.out.println("El articulo del detalle donde estoy parado : " + detalle.getArticulo().getNombreArticulo());
            DetalleOrdenCompra nuevoDetalle = new DetalleOrdenCompra();
            nuevoDetalle.setLinea(detalle.getLinea());

            Articulo a = ArticuloRepo.getReferenceById(detalle.getArticulo().getId());
            System.out.println("El articulo es: " + a.getNombreArticulo()+ "  "+ a.getId());

            Proveedor p = proveedorRepository.findByName(detalle.getProveedor().getNombre());
            System.out.println("El proveedor es: " + p.getNombre()+ "  "+ p.getId());

            //Busco el ProveedrARTICULO PARA CALCULAR EL COSTO DE PEDIDO
            ProveedorArticulo PA = ProveedorArticuloRepo.findByArticuloAndProveedor(p.getId(),a.getId());

            int InventarioMaximo = a.getLoteOptimo() + a.getStockSeguridad();
            System.out.println("el stock de seguridad para  " + a.getNombreArticulo() + " es " + a.getStockSeguridad());
            System.out.println("el lote optimo para " + a.getNombreArticulo() + " es " + a.getLoteOptimo());
            System.out.println("el inventario maximo para " + a.getNombreArticulo() + " es " + InventarioMaximo);

            if( detalle.getCantidad() + a.getCantActual() <= InventarioMaximo){
                detalle.setCantidad(detalle.getCantidad());
                detalle.setSubtotal(detalle.getCantidad() * PA.getCostoPedido());
            } else {
                int cantidadPosta = InventarioMaximo - a.getCantActual() ;
                System.out.println("La cantidad posta es: " + cantidadPosta);
                detalle.setCantidad(cantidadPosta);
                detalle.setSubtotal(cantidadPosta * PA.getCostoPedido());
                System.out.println("el subtotal es: "+ detalle.getSubtotal());
            }
            nuevoDetalle.setSubtotal(detalle.getSubtotal());
            nuevoDetalle.setCantidad(detalle.getCantidad());

            //voy actualizando el total de la OC
            total += detalle.getSubtotal();

            nuevoDetalle.setArticulo(a);
            nuevoDetalle.setProveedor(p);


            DetalleOrdenCompra n = detalleOrdenCompraRepository.save(nuevoDetalle);
            nuevosdetalles.add(n);
            System.out.println("añadi el detalle a la lista de detalles: " + n.getId());

            }
            catch (Exception e) {
                System.err.println("Error: el articulo no es proveido por ese proveedor: " + e.getMessage());
                e.printStackTrace();
            }
        }
        nueva.setTotal(total);

        OrdenCompra Ordennueva = ordenCompraRepository.save(nueva);
        Ordennueva.setDetallesOrdenCompra(nuevosdetalles);

        return ordenCompraRepository.save(Ordennueva);
    }

    public List<OrdenCompra> ListaOrdenes(){ //este metodo se encarga de recuperar las ordenes de compra pendientes o enviadas
        //busco los estados que necesito    //serian las ordenes de compra que se listarian al ingresar a la seccion correspondiente
        System.out.println("Estoy por buscar los estados");
        EstadoOrdenCompra estadoPendiente = estadoOrdenRepository.findByName("Pendiente");
        EstadoOrdenCompra estadoEnviada = estadoOrdenRepository.findByName("Enviada");

        //busco las ordenes de compra que esten en standBy
        List<OrdenCompra> ordenes = ordenCompraRepository.findByState(estadoPendiente.getId(),estadoEnviada.getId());

        System.out.println("Regreso las ordenes encontradas: " + ordenes);

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
        ProveedorArticulo PA = ProveedorArticuloRepo.findByArticuloAndProveedor(p.getId(),detalleOrdenCompra.getArticulo().getId());

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

        EstadoOrdenCompra estadoPendiente = estadoOrdenRepository.findByName("Pendiente");
        OC.setEstadoOrdenCompra(estadoPendiente);
        ordenCompraRepository.save(OC);

        // documento el cambio de estado
        CambioOrdenCompraEstado CambioEstado = new CambioOrdenCompraEstado();
        CambioEstado.setFechaCambio(Date.from(Instant.now()));
        CambioEstado.setOrdenCompra(OC);
        CambioEstado.setEstadoOrdenCompra(estadoPendiente);
        CambioOrdenCompraEstadoRepo.save(CambioEstado);

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
    public OrdenCompra ActualizarEstado(Long ordenCompraId) {
        OrdenCompra OC = ordenCompraRepository.getReferenceById(ordenCompraId);

        if (OC.getEstadoOrdenCompra().getNombreEstado().equals("Enviada")) {
            // busco el estado "Recibida"
            EstadoOrdenCompra estadoRecibida = estadoOrdenRepository.findByName("Recibida");

            // seteo nuevo estado a OC
            OC.setEstadoOrdenCompra(estadoRecibida);
            ordenCompraRepository.save(OC);

            // documento el cambio de estado
            CambioOrdenCompraEstado CambioEstado = new CambioOrdenCompraEstado();
            CambioEstado.setFechaCambio(Date.from(Instant.now()));
            CambioEstado.setOrdenCompra(OC);
            CambioEstado.setEstadoOrdenCompra(estadoRecibida);
            CambioOrdenCompraEstadoRepo.save(CambioEstado);

            /* incremento el inventario de cada producto en la oc (suponiendo que llega todo ok) */
            for (DetalleOrdenCompra detalle : OC.getDetallesOrdenCompra()) {
                Articulo a = detalle.getArticulo();
                a.setCantActual(a.getCantActual() + detalle.getCantidad());
                ArticuloRepo.save(a);
            }
        } else if (OC.getEstadoOrdenCompra().getNombreEstado().equals("Pendiente")) {
            // busco el estado "Enviada"
            EstadoOrdenCompra estadoEnviada = estadoOrdenRepository.findByName("Enviada");

            // seteo nuevo estado a OC
            OC.setEstadoOrdenCompra(estadoEnviada);
            ordenCompraRepository.save(OC);

            // documento el cambio de estado
            CambioOrdenCompraEstado EstadoNuevo = new CambioOrdenCompraEstado();
            EstadoNuevo.setFechaCambio(Date.from(Instant.now()));
            EstadoNuevo.setOrdenCompra(OC);
            EstadoNuevo.setEstadoOrdenCompra(estadoEnviada);
            CambioOrdenCompraEstadoRepo.save(EstadoNuevo);
        }

        return OC;
    }


    public List<DTOProveedorArticulo> BuildDTOData() {
        List<Articulo> a = ArticuloRepo.findAll();
        List<DTOProveedorArticulo> articulos = new ArrayList<>();
        System.out.println("encontre los articulos: " + a.size());

        for (Articulo articulo : a) {
            System.out.println("empiezo a loopear los artiulos: " + articulo.getNombreArticulo());

            // buscar el predeterminado
            Proveedor predeterminado = articulo.getProveedorPredeterminado();
            DTOProveedor Pre = null; // inicializar el DTOProveedor como null

            if (predeterminado != null) {
                // Busco el ProveedorArticulo para este articulo y este proveedor
                ProveedorArticulo PA = ProveedorArticuloRepo.findByArticuloAndProveedor(predeterminado.getId(), articulo.getId());
                System.out.println("Encontre el PA del arcticulo: " + PA);

                if (PA != null) {
                    // construyo el dto para el predeterminado
                    Pre = DTOProveedor.builder()
                            .IdProveedor(predeterminado.getId())
                            .mail(predeterminado.getMail())
                            .costoPedido(PA.getCostoPedido())
                            .direccion(predeterminado.getDireccion())
                            .nombre(predeterminado.getNombre())
                            .telefono(predeterminado.getTelefono())
                            .tiempoPedido(PA.getTiempoPedido())
                            .build();
                    System.out.println("Arme el DTOProveedor predeterminado " + Pre);
                    System.out.println("Arme el DTOProveedor predeterminado " + Pre.getIdProveedor());
                }
            }

            // armo la lista de proveedores para ese articulo
            List<DTOProveedor> proveedores = new ArrayList<>();
            List<ProveedorArticulo> Datos = ProveedorArticuloRepo.findByArticulo(articulo.getId());
            System.out.println("Recupere la lista de PA para ese articulo: " + Datos.size());

            for (ProveedorArticulo proveedorArticulo : Datos) {
                Proveedor Usar = proveedorArticulo.getProveedor();
                DTOProveedor proveedorlista = DTOProveedor.builder()
                        .IdProveedor(Usar.getId())
                        .mail(Usar.getMail())
                        .costoPedido(proveedorArticulo.getCostoPedido())
                        .direccion(Usar.getDireccion())
                        .nombre(Usar.getNombre())
                        .telefono(Usar.getTelefono())
                        .tiempoPedido(proveedorArticulo.getTiempoPedido())
                        .build();
                System.out.println("Arme el DTOProveedor  " + proveedorlista.getIdProveedor());
                proveedores.add(proveedorlista);
                System.out.println("Arme el dto proveedor: " + proveedorlista.nombre);
            }

            // creo el DTOProveedorArticulo
            DTOProveedorArticulo art = DTOProveedorArticulo.builder()
                    .IdArticulo(articulo.getId())
                    .detalle(articulo.getDetalle())
                    .nombreArticulo(articulo.getNombreArticulo())
                    .loteOptimo(articulo.getLoteOptimo())
                    .StockSeguridad(articulo.getStockSeguridad())
                    .listaproveedores(proveedores)
                    .ProveedorPredeterminado(Pre) // puede ser null si no se encuentra el PA
                    .build();
            System.out.println("Arme el DTOProveedorArticulo " + art.getIdArticulo());
            articulos.add(art);
        }
        System.out.println("Termine de armar la lista de articulos: " + articulos.size());
        return articulos;
    }



    @Transactional
    //metodo para el pronostico de demanda probabilistica (genera orden de compra al final de la prediccion si falta stock para satisfacer la demanda
    public OrdenCompra OrdenStockFaltante(Long idArticulo, int cantidadFaltante){
        EstadoOrdenCompra estadoOrdenCompraPen = estadoOrdenRepository.findByName("Pendiente");
        // .orElseThrow(() -> new RuntimeException("Estado Orden de compra no encontrada"));

        EstadoOrdenCompra estadoOrdenCompraEnv = estadoOrdenRepository.findByName("Enviada");
        Articulo a = ArticuloRepo.findById(idArticulo).orElseThrow(() -> new RuntimeException("No se encontro el articulo"));

        // Verifico si existe una orden para el artículo
        List<OrdenCompra> ordenesActivas = ordenCompraRepository.findByArticuloAndEstado(idArticulo, estadoOrdenCompraPen.getId(), estadoOrdenCompraEnv.getId());

        if (!ordenesActivas.isEmpty()) {
            throw new RuntimeException("Ya existe/n"+ ordenesActivas.toArray().length + " orden/es de compra activa/s para el artículo " + a.getNombreArticulo());
        }

        System.out.println("No habian ordenes activas para el articulo  " + a.getNombreArticulo() );
        OrdenCompra nueva = new OrdenCompra();
        nueva.setFechaRealizacion(Date.from(Instant.now()));

        //Determino el Inventario maximo
        int InventarioMaximo = a.getStockSeguridad() + a.getLoteOptimo();
        System.out.println("el stock de seguridad para  " + a.getNombreArticulo() + " es " + a.getStockSeguridad());
        System.out.println("el lote optimo para " + a.getNombreArticulo() + " es " + a.getLoteOptimo());
        System.out.println("el inventario maximo para " + a.getNombreArticulo() + " es " + InventarioMaximo);

        //creo la orden el detalle ode la orden con 1 producto en base al proveedor predeterminado

        ProveedorArticulo PA = ProveedorArticuloRepo.findByArticuloAndProveedor(a.getProveedorPredeterminado().getId(),idArticulo);

        DetalleOrdenCompra detalle = new DetalleOrdenCompra();
        detalle.setArticulo(a);
        detalle.setProveedor(a.getProveedorPredeterminado());
        if( cantidadFaltante + a.getCantActual() <= InventarioMaximo){
            System.out.println("La cantidad faltante es: " + cantidadFaltante);
            detalle.setCantidad(cantidadFaltante);
            detalle.setSubtotal(cantidadFaltante * PA.getCostoPedido());
        } else {
            int cantidadPosta = InventarioMaximo -  a.getCantActual();
            System.out.println("La cantidad posta es: " + cantidadPosta);
            detalle.setCantidad(cantidadPosta);
            detalle.setSubtotal(cantidadPosta * PA.getCostoPedido());
        }

        detalleOrdenCompraRepository.save(detalle);
        List<DetalleOrdenCompra> lista = new ArrayList<DetalleOrdenCompra>();
        lista.add(detalle);

        //busco el estado para la OC
        CambioOrdenCompraEstado CambioEstado = new CambioOrdenCompraEstado();
        CambioEstado.setFechaCambio(Date.from(Instant.now()));
        CambioEstado.setOrdenCompra(nueva);
        CambioOrdenCompraEstadoRepo.save(CambioEstado);

        nueva.setEstadoOrdenCompra(estadoOrdenCompraPen);
        nueva.setDetallesOrdenCompra(lista);
        nueva.setTotal(detalle.getSubtotal());
        ordenCompraRepository.save(nueva);
        System.out.println("Ya cree la OC para el articulo: " + a.getNombreArticulo());
        return nueva;
    }

    @Transactional
    public boolean existeOrden (Long idArticulo){
        EstadoOrdenCompra estadoOrdenCompraPen = estadoOrdenRepository.findByName("Pendiente");
        // .orElseThrow(() -> new RuntimeException("Estado Orden de compra no encontrada"));

        EstadoOrdenCompra estadoOrdenCompraEnv = estadoOrdenRepository.findByName("Enviada");
        Articulo a = ArticuloRepo.findById(idArticulo).orElseThrow(() -> new RuntimeException("No se encontro el articulo"));
        System.out.println("Estoy buscando ordenes para: " + a.getNombreArticulo());
        // Verifico si existe una orden para el artículo
        List<OrdenCompra> ordenesActivas = ordenCompraRepository.findByArticuloAndEstado(idArticulo, estadoOrdenCompraPen.getId(), estadoOrdenCompraEnv.getId());

        if (!ordenesActivas.isEmpty()) {
            System.out.println("Si hay ordenes para:  "  + a.getNombreArticulo() + "en total " + ordenesActivas.size());
            return true;
        }
        System.out.println("No hay ordenes para: " + a.getNombreArticulo());
        return false;
    }

}
