package com.example.demo.services;

import com.example.demo.DTO.ArticuloDTO;
import com.example.demo.entities.Articulo;
import com.example.demo.entities.Demanda;
import com.example.demo.entities.DemandaPronosticada;
import com.example.demo.entities.DetalleOrdenCompra;
import com.example.demo.entities.DetalleVenta;
import com.example.demo.entities.EstadoOrdenCompra;
import com.example.demo.entities.Modelo;
import com.example.demo.entities.OrdenCompra;
import com.example.demo.entities.Proveedor;
import com.example.demo.entities.ProveedorArticulo;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.DemandaRepository;
import com.example.demo.repositories.EstadoOrdenCompraRepository;
import com.example.demo.repositories.ModeloRepository;
import com.example.demo.repositories.OrdenCompraRepository;
import com.example.demo.repositories.ProveedorArticuloRepository;
import com.example.demo.repositories.ProveedorRepository;

import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo, Long> implements ArticuloService {
    @Autowired
    private ArticuloRepository articuloRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private ModeloRepository modeloRepository;
    @Autowired
    private DemandaRepository demandaRepository;
    @Autowired
    private ProveedorArticuloRepository proveedorArticuloRepository;
    @Autowired
    private EstadoOrdenCompraRepository estadoOrdenCompraRepository;
    @Autowired
    private OrdenCompraRepository ordenCompraRepository;


    public ArticuloServiceImpl(BaseRepository<Articulo, Long> baseRepository, ArticuloRepository articuloRepository) {
        super(baseRepository);
        this.articuloRepository = articuloRepository;
    }
    @Transactional
    @Override
    public Articulo agregarProveedorPredeterminado(Duration tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto,Long idArticulo, Long idProveedor) throws Exception {
        try{
            Articulo articulo= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
            Proveedor proveedor=proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            articulo.setProveedorPredeterminado(proveedor);
            ProveedorArticulo pa= articulo.agregarUnProveedorPredeterminado(tiempoPedido,costoPedido,costoAlmacenamiento,costoProducto,proveedor,articulo);
            proveedorArticuloRepository.save(pa);
            articuloRepository.save(articulo);
            return articulo;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    @Transactional
    @Override
    public Articulo agregarModelo(Long idArticulo, Long idModelo) throws Exception {
        try{
            Articulo articulo= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
            Modelo modelo= modeloRepository.findById(idModelo).orElseThrow(() -> new RuntimeException("Modelo no encontrado"));
            articulo.setModelo(modelo);
            articuloRepository.save(articulo);
            return articulo;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }
    @Transactional
    @Override
    public Articulo AsignarUnProveedorAUnArticulo(Duration tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto, Long idArticulo, Long idProveedor) throws Exception {
        try{
            Articulo a= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
            Proveedor p = proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            ProveedorArticulo pa=a.agregarUnProveedor(tiempoPedido,costoPedido,costoAlmacenamiento,costoProducto,p,a);
            proveedorArticuloRepository.save(pa);
            return a;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    @Transactional
    @Override
    public Articulo calcularCGIConProvPredeterminado (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta)throws Exception{
        //BUSQUEDA DE DATOS
        Articulo a= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
        List<Demanda> demandas = demandaRepository.findByArticuloDesdeHasta(idArticulo, periodoDesde, añoDesde, periodoHasta, añoHasta);
        Proveedor pp=a.getProveedorPredeterminado();
        ProveedorArticulo pa= proveedorArticuloRepository.findByArticuloAndProveedor(pp.getId(), idArticulo);
        //ASIGNACION VARIABLES
        a.setCGI(0.0);
        float CGI= 0;
        int D=0;
        for (Demanda d: demandas){
            D += d.getCantTotalDemanda();
        }
        float P=pa.getCostoProducto();
        float Ca=pa.getCostoAlmacenamiento();
        float Q=a.getLoteOptimo();
        float Cp=pa.getCostoPedido();
        float DSobreQ= (float)(D/Q);
        //CALCULO CGI
        CGI = (P * D) + (Ca * (Q / 2))+(Cp*DSobreQ);
        a.setCGI(CGI);
        articuloRepository.save(a);
        return a;
    }
    @Transactional
    @Override
    public Articulo calcularCGI (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, Long idProveedor)throws Exception{
        //BUSQUEDA DE DATOS
        Articulo a= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
        List<Demanda> demandas = demandaRepository.findByArticuloDesdeHasta(idArticulo, periodoDesde, añoDesde, periodoHasta, añoHasta);
        Proveedor p= proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        ProveedorArticulo pa= proveedorArticuloRepository.findByArticuloAndProveedor(idProveedor, idArticulo);
        int D = 0;  
        float CGI= 0 ;
        for (Demanda demanda: demandas){
            D= D + demanda.getCantTotalDemanda();
        }
        float P=pa.getCostoProducto();
        float Ca=pa.getCostoAlmacenamiento();
        float Q=a.getLoteOptimo();
        float Cp=pa.getCostoPedido();
        float DSobreQ= (float)(D/Q);
        //CALCULO CGI
        CGI= ((P*D) + (Ca*(Q/2)) + (Cp*DSobreQ));
        a.setCGI(CGI);
        articuloRepository.save(a);
        return a;
    }

    public long calcularDias(int mesDesde, int mesHasta,int añoDesde,int añoHasta){
        LocalDate fechaDesde = LocalDate.of(añoDesde, mesDesde, 1);
        LocalDate fechaHasta = LocalDate.of(añoHasta, mesHasta, 1);
        fechaHasta = fechaHasta.plusMonths(1).minusDays(1);
        long dias = ChronoUnit.DAYS.between(fechaDesde, fechaHasta);
        return dias;
    }
    @Transactional
    @Override
    public Articulo LoteFijoConProveedor (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, Long idProveedor, float DPromedio, float DDesvEstandar, double Z)throws Exception{
        //BUSQUEDA DE DATOS
        Articulo a= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
        List<Demanda> demandas = demandaRepository.findByArticuloDesdeHasta(idArticulo, periodoDesde, añoDesde, periodoHasta, añoHasta);
        Proveedor p= proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        ProveedorArticulo pa= proveedorArticuloRepository.findByArticuloAndProveedor(idProveedor, idArticulo);
        int D = 0;  
        for (Demanda demanda: demandas){
            D= D + demanda.getCantTotalDemanda();
        }
        float P=pa.getCostoProducto();
        float Ca=pa.getCostoAlmacenamiento();
        float Cp=pa.getCostoPedido();
        Duration dias=pa.getTiempoPedido();
        //calculo lote optimo
        int Q=(int) Math.sqrt(2*D*(Cp/Ca));
        a.setLoteOptimo(Q);
        //Calculo stock seguridad
        Duration duracionPedidoDuration = pa.getTiempoPedido();
        long tiempoPedidoEnDias = duracionPedidoDuration.toDays();
        double DesvEstandarL=(DDesvEstandar*Math.sqrt(tiempoPedidoEnDias));
        double SS= DesvEstandarL*Z;
        a.setStockSeguridad((int)SS);
        //calculo punto pedido
        float demandaFloat= (float)D;
        long diasTrabajoEnElPeriodo=calcularDias(periodoDesde,periodoHasta,añoDesde,añoHasta);
        float d = demandaFloat/diasTrabajoEnElPeriodo;
        int puntoPedido= (int) (d*tiempoPedidoEnDias);
        a.setPuntoPedido(puntoPedido);
        articuloRepository.save(a);
        return a;
    }
    @Transactional
    @Override
    public Articulo LoteFijoConProveedorPredeterminado (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, float DPromedio, float DDesvEstandar, double Z)throws Exception{
        //BUSQUEDA DE DATOS
        Articulo a= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
        List<Demanda> demandas = demandaRepository.findByArticuloDesdeHasta(idArticulo, periodoDesde, añoDesde, periodoHasta, añoHasta);
        Proveedor p= a.getProveedorPredeterminado();
        ProveedorArticulo pa= proveedorArticuloRepository.findByArticuloAndProveedor(p.getId(), idArticulo);
        int D = 0;  
        for (Demanda demanda: demandas){
            D= D + demanda.getCantTotalDemanda();
        }
        float P=pa.getCostoProducto();
        float Ca=pa.getCostoAlmacenamiento();
        float Cp=pa.getCostoPedido();
        Duration dias=pa.getTiempoPedido();
        //calculo lote optimo
        int Q=(int) Math.sqrt(2*D*(Cp/Ca));
        a.setLoteOptimo(Q);
        //Calculo stock seguridad
        Duration duracionPedidoDuration = pa.getTiempoPedido();
        long tiempoPedidoEnDias = duracionPedidoDuration.toDays();
        double DesvEstandarL=(DDesvEstandar*Math.sqrt(tiempoPedidoEnDias));
        double SS= DesvEstandarL*Z;
        a.setStockSeguridad((int)SS);
        //calculo punto pedido
        float demandaFloat= (float)D;
        long diasTrabajoEnElPeriodo=calcularDias(periodoDesde,periodoHasta,añoDesde,añoHasta);
        float d = demandaFloat/diasTrabajoEnElPeriodo;
        int puntoPedido= (int) (d*tiempoPedidoEnDias);
        a.setPuntoPedido(puntoPedido);
        articuloRepository.save(a);
        return a;
    }
    @Transactional
    @Override
    public Articulo IntervaloFijoConProveedor (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, Long idProveedor, float DPromedio, float DDesvEstandar, double Z, Duration periodo)throws Exception{
        //BUSQUEDA DE DATOS
        Articulo a= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
        Modelo modelo= a.getModelo();
        //if(modelo.getNombreModelo()!="INTERVALO_FIJO"){
        //    throw new IllegalArgumentException("El modelo del artículo no es INTERVALO_FIJO");
        //}
        List<Demanda> demandas = demandaRepository.findByArticuloDesdeHasta(idArticulo, periodoDesde, añoDesde, periodoHasta, añoHasta);
        Proveedor p= proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        ProveedorArticulo pa= proveedorArticuloRepository.findByArticuloAndProveedor(idProveedor, idArticulo);
        int D = 0;  
        for (Demanda demanda: demandas){
            D= D + demanda.getCantTotalDemanda();
        }
        Duration duracionPedidoDuration = pa.getTiempoPedido();
        long tiempoPedidoEnDias = duracionPedidoDuration.toDays();
        long tiempoPeriodoEnDias = periodo.toDays();
        double DesvEstandarLT=(DDesvEstandar*Math.sqrt(tiempoPedidoEnDias+tiempoPeriodoEnDias));
        double SS= DesvEstandarLT*Z;
        a.setStockSeguridad((int)SS);
        //calculo punto pedido
        float demandaFloat= (float)D;
        long diasTrabajoEnElPeriodo=calcularDias(periodoDesde,periodoHasta,añoDesde,añoHasta);
        float d = demandaFloat/diasTrabajoEnElPeriodo;
        int puntoPedido= (int) (d*(tiempoPedidoEnDias+tiempoPeriodoEnDias));
        a.setPuntoPedido(puntoPedido);
        //calculo lote optimo
        int Q= (int)(puntoPedido+(Z*DesvEstandarLT)-a.getCantActual());
        a.setLoteOptimo(Q);
        articuloRepository.save(a);
        return a;
    }
    @Transactional
    @Override
    public Articulo IntervaloFijoConProveedorPredeterminado (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, float DPromedio, float DDesvEstandar, double Z, Duration periodo)throws Exception{
        //BUSQUEDA DE DATOS
        Articulo a= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
        Modelo modelo= a.getModelo();
        //if(modelo.getNombreModelo()!="INTERVALO_FIJO"){
        //    throw new IllegalArgumentException("El modelo del artículo no es INTERVALO_FIJO");
        //}
        List<Demanda> demandas = demandaRepository.findByArticuloDesdeHasta(idArticulo, periodoDesde, añoDesde, periodoHasta, añoHasta);
        Proveedor p= a.getProveedorPredeterminado();
        ProveedorArticulo pa= proveedorArticuloRepository.findByArticuloAndProveedor(p.getId(), idArticulo);
        int D = 0;  
        for (Demanda demanda: demandas){
            D= D + demanda.getCantTotalDemanda();
        }
        Duration duracionPedidoDuration = pa.getTiempoPedido();
        long tiempoPedidoEnDias = duracionPedidoDuration.toDays();
        long tiempoPeriodoEnDias = periodo.toDays();
        double DesvEstandarLT=(DDesvEstandar*Math.sqrt(tiempoPedidoEnDias+tiempoPeriodoEnDias));
        double SS= DesvEstandarLT*Z;
        a.setStockSeguridad((int)SS);
        //calculo punto pedido
        float demandaFloat= (float)D;
        long diasTrabajoEnElPeriodo=calcularDias(periodoDesde,periodoHasta,añoDesde,añoHasta);
        float d = demandaFloat/diasTrabajoEnElPeriodo;
        int puntoPedido= (int) (d*(tiempoPedidoEnDias+tiempoPeriodoEnDias));
        a.setPuntoPedido(puntoPedido);
        //calculo lote optimo
        int Q= (int)(puntoPedido+(Z*DesvEstandarLT)-a.getCantActual());
        a.setLoteOptimo(Q);
        articuloRepository.save(a);
        return a;
    }
    public List<ArticuloDTO> ListadoDeArticulosFaltantes() throws Exception {
        try{
            List<Articulo> articulos=articuloRepository.articulosFaltantes();
            List<ArticuloDTO> articulosDTO= new ArrayList<>();
            for (Articulo a: articulos){
                ArticuloDTO articulodto =new ArticuloDTO(a.getNombreArticulo(),a.getLoteOptimo(), a.getPuntoPedido(),a.getStockSeguridad(),a.getCantActual());
                articulosDTO.add(articulodto);
            }
            return articulosDTO;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    @Transactional
    @Override
    public List<ArticuloDTO> ListadoDeArticulosAReponer() throws Exception {
        try{
            List<Articulo> articulosAReponer=articuloRepository.articulosAReponer();
            List<ArticuloDTO> articulosDTO= new ArrayList<>();
            List<OrdenCompra> OCPendientes= ordenCompraRepository.findByEstado("Pendiente");
            for(OrdenCompra ocp : OCPendientes){
                List<DetalleOrdenCompra> detallesOCPendientes=ocp.getDetallesOrdenCompra();
                for(DetalleOrdenCompra docp:detallesOCPendientes){
                    Articulo docpa=docp.getArticulo();
                    for(Articulo a:articulosAReponer){
                        if(docpa.getId()==a.getId()){
                            ArticuloDTO articulodto=new ArticuloDTO(a.getNombreArticulo(),a.getLoteOptimo(),a.getPuntoPedido(),a.getStockSeguridad(),a.getCantActual());
                            articulosDTO.add(articulodto);
                        }
                    }
                }
            }
            return articulosDTO;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }


    
}
