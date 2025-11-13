package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {

    @Query("SELECT m FROM Marca m WHERE m.proveedor.idProveedor = :idProveedor")
    List<Marca> findByProveedorId(Integer idProveedor);

    Optional<Marca> findByNombreMarca(String nombreMarca);

    boolean existsByNombreMarca(String nombreMarca);

    boolean existsByNombreMarcaAndIdMarcaNot(String nombreMarca, Integer idMarca);
}