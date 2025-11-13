package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    boolean existsByNombreCategoria(String nombreCategoria);
    boolean existsByNombreCategoriaAndIdCategoriaNot(String nombreCategoria, Integer idCategoria);
}
