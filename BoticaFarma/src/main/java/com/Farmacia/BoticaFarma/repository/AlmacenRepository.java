package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {
}