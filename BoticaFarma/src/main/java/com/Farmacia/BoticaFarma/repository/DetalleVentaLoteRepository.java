package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.DetalleVentaLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaLoteRepository extends JpaRepository<DetalleVentaLote, Integer> {
    // Por ahora no necesitas métodos adicionales
}