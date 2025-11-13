package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Detalle_venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<Detalle_venta, Integer> {

    // Listar detalles de una venta específica
    List<Detalle_venta> findByVenta_IdVenta(Integer idVenta);
}