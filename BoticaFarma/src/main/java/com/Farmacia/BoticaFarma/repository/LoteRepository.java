package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {

    // Listar lotes de un producto (FIFO)
    List<Lote> findByProducto_IdProductoOrderByFechaIngresoDesc(Integer idProducto);
    List<Lote> findByProducto_IdProductoOrderByFechaIngresoAsc(Integer idProducto);
    // Calcular stock total
    @Query("SELECT COALESCE(SUM(l.cantidadActual), 0) FROM Lote l WHERE l.producto.idProducto = :idProducto")
    Integer calcularStockTotal(@Param("idProducto") Integer idProducto);

    // AGREGAR: Contar lotes por fecha para generar correlativo
    @Query("SELECT COUNT(l) FROM Lote l WHERE l.fechaIngreso = :fecha")
    Integer contarLotesPorFecha(@Param("fecha") LocalDate fecha);

}