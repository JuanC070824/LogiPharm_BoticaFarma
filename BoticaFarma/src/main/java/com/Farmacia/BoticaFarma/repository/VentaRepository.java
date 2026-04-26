package com.Farmacia.BoticaFarma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Farmacia.BoticaFarma.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    // CAMBIO: Ahora devuelve List<Object[]>
    // Quitamos el "new com.Farmacia..." y dejamos solo los datos
    @Query("SELECT FUNCTION('DATE_FORMAT', v.fecha, '%Y-%m-%d'), SUM(v.total) " +
            "FROM Venta v GROUP BY FUNCTION('DATE_FORMAT', v.fecha, '%Y-%m-%d')")
    List<Object[]> obtenerVentasDiarias();

    @Query("SELECT FUNCTION('DATE_FORMAT', v.fecha, '%Y-%m'), SUM(v.total) " +
            "FROM Venta v GROUP BY FUNCTION('DATE_FORMAT', v.fecha, '%Y-%m')")
    List<Object[]> obtenerVentasMensuales();
}