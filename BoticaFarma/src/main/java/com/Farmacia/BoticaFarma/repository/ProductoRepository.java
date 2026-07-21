package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // 1. Obtener todos los productos de una botica especifica
    List<Producto> findByBotica_IdBotica(Integer idBotica);

    // 2. Buscar por nombre filtrado por Botica
    Page<Producto> findByBotica_IdBoticaAndNombreProductoContainingIgnoreCase(Integer idBotica, String nombre, Pageable pageable);

    // 3. Filtrar por categoría y Botica
    Page<Producto> findByBotica_IdBoticaAndCategoria_IdCategoria(Integer idBotica, Integer idCategoria, Pageable pageable);

    // 4. Filtrar por marca y Botica
    Page<Producto> findByBotica_IdBoticaAndMarca_IdMarca(Integer idBotica, Integer idMarca, Pageable pageable);

    // 5. Búsqueda avanzada con filtros múltiples + Botica
    @Query("SELECT p FROM Producto p " +
            "LEFT JOIN p.categoria c " +
            "LEFT JOIN p.marca m " +
            "WHERE p.botica.idBotica = :idBotica " +
            "AND (:nombre IS NULL OR LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
            "AND (:idCategoria IS NULL OR c.idCategoria = :idCategoria) " +
            "AND (:idMarca IS NULL OR m.idMarca = :idMarca)")
    Page<Producto> buscarConFiltros(
            @Param("idBotica") Integer idBotica,
            @Param("nombre") String nombre,
            @Param("idCategoria") Integer idCategoria,
            @Param("idMarca") Integer idMarca,
            Pageable pageable
    );

    // 6. Obtener productos con stock bajo filtrado por Botica
    @Query("SELECT p FROM Producto p WHERE p.botica.idBotica = :idBotica AND p.Stock < :minStock")
    Page<Producto> findProductosStockBajo(@Param("idBotica") Integer idBotica, @Param("minStock") Integer minStock, Pageable pageable);
}