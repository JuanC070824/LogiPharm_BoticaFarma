package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Buscar por nombre (✔ corregido)
    Page<Producto> findByNombreProductoContainingIgnoreCase(String nombre, Pageable pageable);

    // Filtrar por categoría
    Page<Producto> findByCategoria_IdCategoria(Integer idCategoria, Pageable pageable);

    // Filtrar por marca
    Page<Producto> findByMarca_IdMarca(Integer idMarca, Pageable pageable);

    // Búsqueda avanzada con filtros múltiples (✔ corregido)
    @Query("SELECT p FROM Producto p " +
            "LEFT JOIN p.categoria c " +
            "LEFT JOIN p.marca m " +
            "WHERE (:nombre IS NULL OR LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
            "AND (:idCategoria IS NULL OR c.idCategoria = :idCategoria) " +
            "AND (:idMarca IS NULL OR m.idMarca = :idMarca)")
    Page<Producto> buscarConFiltros(
            @Param("nombre") String nombre,
            @Param("idCategoria") Integer idCategoria,
            @Param("idMarca") Integer idMarca,
            Pageable pageable
    );

    // Obtener productos con stock bajo
    @Query("SELECT p FROM Producto p WHERE p.Stock < :minStock")
    Page<Producto> findProductosStockBajo(@Param("minStock") Integer minStock, Pageable pageable);
}
