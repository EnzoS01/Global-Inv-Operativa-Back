package com.example.demo.services;

import com.example.demo.entities.Articulo;
import com.example.demo.entities.DetalleVenta;
import com.example.demo.entities.Modelo;
import com.example.demo.entities.OrdenCompra;
import com.example.demo.repositories.ArticuloRepository;
import com.example.demo.repositories.BaseRepository;
import com.example.demo.repositories.DetalleVentaRepository;
import com.example.demo.repositories.ModeloRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
public class DetalleVentaServiceImpl extends BaseServiceImpl<DetalleVenta,Long> implements DetalleVentaService {
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    @Autowired
    private ArticuloRepository articuloRepository;
    @Autowired
    private OrdenCompraServiceImpl OrdenCompraService;
    @Autowired
    private ModeloRepository ModeloRepo;

    public DetalleVentaServiceImpl(BaseRepository<DetalleVenta, Long> baseRepository, DetalleVentaRepository detalleVentaRepository) {
        super(baseRepository);
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Transactional
    public DetalleVenta agregarArticulo(Long detalleVentaId, Long articuloId){
        try {
            Articulo articulo = articuloRepository.findById(articuloId)
                    .orElseThrow(() -> new RuntimeException("Articulo no encontrado"));

            DetalleVenta detalleVenta = detalleVentaRepository.findById(detalleVentaId)
                    .orElseThrow(() -> new RuntimeException("DetalleVenta no encontrado"));

            articulo.setCantActual(articulo.getCantActual() - detalleVenta.getCantidad());

            articuloRepository.save(articulo);

            detalleVenta.setSubtotal(articulo.getPrecioVenta() * detalleVenta.getCantidad());
            detalleVenta.setArticulo(articulo);

            DetalleVenta detalleActualizado = detalleVentaRepository.save(detalleVenta);

            // En base al modelo del articulo verifico si pido o no
            Modelo m = ModeloRepo.FindByNombre("LOTE_FIJO");
            if(!OrdenCompraService.existeOrden(articulo.getId())){
                try {
                    if (articulo.getModelo().equals(m)) {
                        if (articulo.getCantActual() <= articulo.getPuntoPedido()) {
                            // Crear orden de compra para lote fijo
                            OrdenCompra o = OrdenCompraService.OrdenStockFaltante(articulo.getId(), articulo.getLoteOptimo());
                        }
                    } else {
                        // si es intervalo fijo, verifico la feha de reposiscion y luego verifico si existe una orden en curso
                        LocalDate fechaActual = LocalDate.now();
                        LocalDate proximaFechaReposicion = fechaActual.withDayOfMonth(4); //with(TemporalAdjusters.firstDayOfMonth());
                        //si quiero cambiar el dia de la reposicion debo hacer: LocalDate proximaFechaReposicion = fechaActual.withDayOfMonth(4);
                        System.out.println("La fecha actual es: " + fechaActual);
                        System.out.println("La fecha de la reposicion es: " + proximaFechaReposicion);

                        if (fechaActual.isEqual(proximaFechaReposicion)) {
                            if (!OrdenCompraService.existeOrden(articulo.getId())) {
                                // Calcular cantidad necesaria
                                int nivelMaximo = articulo.getLoteOptimo() + articulo.getStockSeguridad();
                                int cantidadNecesaria = nivelMaximo - articulo.getCantActual();

                                // Crear orden de compra para intervalo fijo
                                OrdenCompra o = OrdenCompraService.OrdenStockFaltante(articulo.getId(), cantidadNecesaria);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error al generar orden de compra: " + e.getMessage());
                    e.printStackTrace();
                }

            }

            return detalleActualizado;
        } catch (Exception e) {
            System.err.println("Error en agregarArticulo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
