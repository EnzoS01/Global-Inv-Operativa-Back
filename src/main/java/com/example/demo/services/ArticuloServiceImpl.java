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
    public Articulo agregarProveedorPredeterminado(Long idArticulo, Long idProveedor) throws Exception{
        Articulo articulo= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
        Proveedor prov= proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        proveedorArticuloRepository.findByArticuloAndProveedor(idProveedor, idArticulo);
        articulo.setProveedorPredeterminado(prov);
        articuloRepository.save(articulo);
        return articulo;
    }

    @Transactional
    @Override
    public Articulo agregarProveedorPredeterminado(int tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto,Long idArticulo, Long idProveedor) throws Exception {
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
    public Articulo AsignarUnProveedorAUnArticulo(int tiempoPedido, float costoPedido, float costoAlmacenamiento, float costoProducto, Long idArticulo, Long idProveedor) throws Exception {
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


    public Articulo calcularLoteFijo(Articulo a,int D,float P,float Ca, float Cp,int tiempoPedido, float DDesvEstandar, double Z,long diasTrabajoEnElPeriodo)throws Exception{
        //calculo lote optimo
        int Q=(int) Math.sqrt(2*D*(Cp/Ca));
        a.setLoteOptimo(Q);
        //Calculo stock seguridad
        double DesvEstandarL=(DDesvEstandar*Math.sqrt(tiempoPedido));
        double SS= DesvEstandarL*Z;
        a.setStockSeguridad((int)SS);
        //calculo punto pedido
        float demandaFloat= (float)D;
        float d = demandaFloat/diasTrabajoEnElPeriodo;
        int puntoPedido= (int) (d*tiempoPedido);
        a.setPuntoPedido(puntoPedido);
        articuloRepository.save(a);
        return a;
    }


    public Articulo calcularIntervaloFijo(Articulo a,int D,int tiempoPedido, float DDesvEstandar, double Z,int tiempoPeriodoEnDias,long diasTrabajoEnElPeriodo)throws Exception{
         //calculo stock de seguridad
         double DesvEstandarLT=(DDesvEstandar*Math.sqrt(tiempoPedido+tiempoPeriodoEnDias));
         double SS= DesvEstandarLT*Z;
         a.setStockSeguridad((int)SS);
         //calculo punto pedido
         float demandaFloat= (float)D;
         float d = demandaFloat/diasTrabajoEnElPeriodo;
         int puntoPedido= (int) (d*(tiempoPedido+tiempoPeriodoEnDias));
         a.setPuntoPedido(puntoPedido);
         //calculo lote optimo
         int Q= (int)(puntoPedido+(Z*DesvEstandarLT)-a.getCantActual());
         a.setLoteOptimo(Q);
         articuloRepository.save(a);
         return a;
    }
    @Transactional
    @Override
    public Articulo ModeloConProveedorPredeterminado(Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, float DDesvEstandar, double Z)throws Exception{
         //BUSQUEDA DE DATOS
         Articulo a= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
         List<Demanda> demandas = demandaRepository.findByArticuloDesdeHasta(idArticulo, periodoDesde, añoDesde, periodoHasta, añoHasta);
         Proveedor p= a.getProveedorPredeterminado();
         ProveedorArticulo pa= proveedorArticuloRepository.findByArticuloAndProveedor(p.getId(), idArticulo);
         int tiempoPeriodoEnDias= a.getPeriodo();
         int D = 0;  
         for (Demanda demanda: demandas){
             D= D + demanda.getCantTotalDemanda();
         }
         float P=pa.getCostoProducto();
         float Ca=pa.getCostoAlmacenamiento();
         float Cp=pa.getCostoPedido();
         int tiempoPedido=pa.getTiempoPedido();
         Modelo modelo = a.getModelo();
         long diasTrabajoEnElPeriodo=calcularDias(periodoDesde,periodoHasta,añoDesde,añoHasta);
         if(modelo.getNombreModelo()=="LOTE_FIJO"){
             a=calcularLoteFijo(a,D,P,Ca,Cp,tiempoPedido,DDesvEstandar,Z,diasTrabajoEnElPeriodo);
         }else if(modelo.getNombreModelo()=="INTERVALO_FIJO"){
             a=calcularIntervaloFijo(a,D,tiempoPedido,DDesvEstandar,Z,tiempoPeriodoEnDias,diasTrabajoEnElPeriodo);
         }
         return a;
     }

    @Transactional
    @Override
    public Articulo ModeloConProveedor (Long idArticulo ,int añoDesde ,int añoHasta ,int periodoDesde ,int periodoHasta, Long idProveedor, float DDesvEstandar, double Z)throws Exception{
        //BUSQUEDA DE DATOS
        Articulo a= articuloRepository.findById(idArticulo).orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
        List<Demanda> demandas = demandaRepository.findByArticuloDesdeHasta(idArticulo, periodoDesde, añoDesde, periodoHasta, añoHasta);
        Proveedor p= proveedorRepository.findById(idProveedor).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        ProveedorArticulo pa= proveedorArticuloRepository.findByArticuloAndProveedor(idProveedor, idArticulo);
        int tiempoPeriodoEnDias= a.getPeriodo();
        int D = 0;  
        for (Demanda demanda: demandas){
            D= D + demanda.getCantTotalDemanda();
        }
        float P=pa.getCostoProducto();
        float Ca=pa.getCostoAlmacenamiento();
        float Cp=pa.getCostoPedido();
        int tiempoPedido=pa.getTiempoPedido();
        Modelo modelo = a.getModelo();
        long diasTrabajoEnElPeriodo=calcularDias(periodoDesde,periodoHasta,añoDesde,añoHasta);
        if(modelo.getNombreModelo()=="LOTE_FIJO"){
            a=calcularLoteFijo(a,D,P,Ca,Cp,tiempoPedido,DDesvEstandar,Z,diasTrabajoEnElPeriodo);
        }else if(modelo.getNombreModelo()=="INTERVALO_FIJO"){
            a=calcularIntervaloFijo(a,D,tiempoPedido,DDesvEstandar,Z,tiempoPeriodoEnDias,diasTrabajoEnElPeriodo);
        }
        return a;
    }
    @Transactional
    @Override
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

    @Override
    public Articulo findByname(String nombre) throws Exception {
        return articuloRepository.findByName(nombre);
    }
}
