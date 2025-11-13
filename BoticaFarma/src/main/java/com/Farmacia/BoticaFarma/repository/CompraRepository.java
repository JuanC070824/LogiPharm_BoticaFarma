package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {
    // Por ahora vacío, Spring genera automáticamente los métodos básicos
}