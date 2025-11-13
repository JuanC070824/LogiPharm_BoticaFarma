package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {


    List<Venta> findByUsuario_IdUsuarioOrderByFechaDesc(Integer idUsuario);
}