package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {

    // Spring Data JPA crea la consulta automáticamente mapeando la relación botica -> idBotica
    List<Almacen> findByBoticaIdBotica(Integer idBotica);
}